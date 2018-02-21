/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package I2CCommunication;

import Commands.Acceleration;
import Commands.Calibrate;
import Commands.Commando;
import Commands.Light;
import Commands.Move;
import Commands.StateRequest;
import Commands.Suction;
import Commands.Velocity;
import Status.Busy;
import Status.EMC;
import Status.ElevatorLimitTrigg;
import Status.EncoderOutOfRange;
import Status.EncoderOutOfSync;
import Status.FlagPos;
import Status.LinearBotLimitTrigged;
import Status.ReadyToRecieve;
import Status.SafetySwitchLower;
import Status.SafetySwitchUpper;
import Status.Status;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.LinkedList;

/**
 * This communication class holds the respectively i2c devices used for the i2c
 * communication. Sending and recieving via i2c should be done via this class.
 * It works as an information relay. Recieve commando and send the appropriate
 * data to the respective controllers.
 *
 *
 * @author PerEspen
 */
public class I2CCommunication implements Runnable
{

    /*FROM THE ARDUINO/Communication TO THE JAVA PROGRAM*/
    private static final byte BUSY = 0x50;
    private static final byte READY_TO_RECIEVE = 0x51;
    private static final byte EMC = 0x60;
    private static final byte SAFETY_SWITCH_UPPER = 0x61;
    private static final byte SAFETY_SWITCH_LOWER = 0x62;
    private static final byte ELEV_LIMIT_TRIGG = 0x63;
    private static final byte LINEARBOT_LMIT_TRIGG = 0x64;
    private static final byte ENCODER_OUT_OF_SYNC = 0x65;
    private static final byte ENCODER_OUT_OF_RANGE = 0x66;
    private static final byte CALIB_PARAM = 0x70;
    private static final byte FLAG_POS = 0x71;

    //i2c-dev bus used
    private static final int I2CbusNr = 4;
    private static final byte CONTROLLER_ADDR_ELEVATOR = 0x01;
    private static final byte CONTROLLER_ADDR_LINEARBOT = 0x02;

    //I2C Bus
    I2CBus i2cbus;
    //Controllers
    I2CDevice linearRobot;
    I2CDevice elevatorRobot;

    // boolean waitingLinearState;
    // boolean waitingElevatorState;
    Status elevatorState;
    Status linearBotState;

    //Lists to keep incomming demands in queue
    LinkedList<Commando> sendQeue;
    LinkedList<Commando> recieveQeue;

    HashMap<Byte, Status> statusMap;

    public I2CCommunication()
    {
        //Create the recieve send lists
        recieveQeue = new LinkedList<Commando>();
        sendQeue = new LinkedList<Commando>();
        //hashmap for status vs byte value
        fillStatusMap(statusMap);
        initiate();
    }

    @Override
    public void run()
    {
        while(true)
        {    
        if (!sendQeue.isEmpty())
        {    //Send the commands in the qeue
            sendCommand(sendQeue.pollLast());
            // Only recieve if something is sent
            //TODO: Check this, currently the incomming recieving qeue only can recieve stateRequest, maybe staterequest should be Status 
            //and thereof the incomming demand can handle all kind of "requests" for different states
            if (!recieveQeue.isEmpty())
            {//read the expecting incomming bytes
                //Find the status to create and put all the values in the status
                //Trigger the status listener
                requestStatus(recieveQeue.pollLast());
                checkStatesAndTrigger(elevatorState, linearBotState);

            }
        }
        }

    }

    private void fillStatusMap(HashMap statusMap)
    {
        statusMap.put(BUSY, new Busy(BUSY, 1));
        statusMap.put(EMC, new EMC(EMC, 1));
        statusMap.put(ELEV_LIMIT_TRIGG, new ElevatorLimitTrigg(ELEV_LIMIT_TRIGG, 1));
        statusMap.put(ENCODER_OUT_OF_RANGE, new EncoderOutOfRange(ENCODER_OUT_OF_RANGE, 1));
        statusMap.put(ENCODER_OUT_OF_SYNC, new EncoderOutOfSync(ENCODER_OUT_OF_SYNC, 1));
        statusMap.put(FLAG_POS, new FlagPos(FLAG_POS, 1));
        statusMap.put(LINEARBOT_LMIT_TRIGG, new LinearBotLimitTrigged(LINEARBOT_LMIT_TRIGG, 1));
        statusMap.put(READY_TO_RECIEVE, new ReadyToRecieve(READY_TO_RECIEVE, 1));
        statusMap.put(SAFETY_SWITCH_LOWER, new SafetySwitchLower(SAFETY_SWITCH_LOWER, 1));
        statusMap.put(SAFETY_SWITCH_UPPER, new SafetySwitchUpper(SAFETY_SWITCH_UPPER, 1));
    }

    /**
     * Handle the task of sending StateRequest to the defined controllers Reads
     * the return bytes and makes a state of them Updates the global state for
     * each respective controller
     *
     * @param request The given StateRequest
     */
    private void requestStatus(Commando request)
    {

        //Storing of bytes
        byte[] returnByteLinearBot = null;
        byte[] returnByteElevator = null;
        //TODO: Fix this staterequest, should maybe be commando
        StateRequest cmdStqry = (StateRequest) request;

        //Check if StateRequest is for elevator robot
        if (cmdStqry.forElevatorRobot())
        {
            returnByteElevator = readByteFromAddr(elevatorRobot, cmdStqry.getCmdAddr(), 5);
            elevatorState = makeState(returnByteElevator);
        }
        //Check if StateRequest is for linear robot
        if (cmdStqry.forLinearRobot())
        {
            returnByteLinearBot = readByteFromAddr(linearRobot, cmdStqry.getCmdAddr(), 5);
            linearBotState = makeState(returnByteLinearBot);
        }

        //Find the retrievend command and preform the State Update
        //updateState(cmdReg.findCommand(returnByteElevator[0]));
        // updateState(cmdReg.findCommand(returnByteLinear[0]));
        // checkState(cmdReg.findCommand(returnByteElevator[0]), cmdReg.findCommand(returnByteLinear[0]));
        //Reset the state request
        //((StateRequest) cmd).reset();
    }

    /**
     * Check for the readytorecieve state and trigger if both are ready
     * Else trigger the states which are not ready to recieve
     * @param elevatorState The elevator State
     * @param linearBotState The linearbot state
     */
    private void checkStatesAndTrigger(Status elevatorState, Status linearBotState)
    {
        if (elevatorState.getClass().equals(ReadyToRecieve.class) && linearBotState.getClass().equals(ReadyToRecieve.class))
        {
            //TODO: Trigger ready to recieve
        } else if (!(elevatorState.getClass().equals(ReadyToRecieve.class)))
        {
            //TODO: Trigger this state
        } else if (!(linearBotState.getClass().equals(ReadyToRecieve.class)))
        {
            //TODO: Trigger this state
        }
    }

    /**
     * Make a state from the given statebyte[]
     *
     * @param stateByte Statebyte to create state from
     * @return Returns the created state, else null!
     */
    public Status makeState(byte[] stateByte)
    {
        Status returnState = statusMap.get(stateByte[0]);
        if (returnState != null)
        {
            returnState.putValue(stateByte);
        }

        return returnState;
    }

    /**
     * Add to the sendqueue, only commands
     *
     * @param cmd Commando to be performed
     */
    public void addSendQ(Commando cmd)
    {
        sendQeue.add(cmd);
    }

    /**
     * Added to the recieving queue
     *
     * @param stat
     */
    //TODO: Make changes to this recieving thing
    public void addRecieveQ(StateRequest stat)
    {
        recieveQeue.add(stat);
    }

    /**
     * Sets up the I2C bus with platform and initiates the connection
     */
    private void initiate()
    {
        try
        {
            try
            {
                PlatformManager.setPlatform(Platform.ODROID);

            } catch (PlatformAlreadyAssignedException ex)
            {
                Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
            // get the I2C bus to communicate on
            i2cbus = I2CFactory.getInstance(I2CbusNr);
            elevatorRobot = i2cbus.getDevice(CONTROLLER_ADDR_ELEVATOR);
            linearRobot = i2cbus.getDevice(CONTROLLER_ADDR_LINEARBOT);

        } catch (I2CFactory.UnsupportedBusNumberException ex)
        {
            Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read the incomming message from the device
     *
     * @param device The device to read from
     * @return Return the incomming byte from the
     */
    private byte readByte(I2CDevice device)
    {
        return readByte(device);
    }

    /**
     * Read from the given register address and buffer the answer in the byte[]
     *
     * @param device Device to read from
     * @param address The register address specified
     * @param byteSize Size of the buffer byte
     * @return Returns a read buffer from the given i2cdevice with given
     * bytesize
     */
    private byte[] readByteFromAddr(I2CDevice device, byte address, int byteSize)
    {
        //Fields
        byte[] returnByte = new byte[byteSize];
        int offset = 0;
        //Store number of bytes actually read
        int bytesRead = 0;
        try
        {
            bytesRead = device.read(address, returnByte, offset, byteSize);

        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //create array with exact amount of bytes
        byte[] modifiedReturnByte = new byte[bytesRead];
        //Transfer the bytes
        for (int i = 0; i <= bytesRead; i++)
        {
            modifiedReturnByte[i] = returnByte[i];
        }
        //return the byte[]
        return modifiedReturnByte;
    }

    /**
     * Read from the given register address and buffer the answer in the byte[]
     *
     * @param device Device to read from
     * @param address The register address specified
     * @param byteSize Size of the buffer byte
     * @return Returns a read buffer from the given i2cdevice with given
     * bytesize
     */
    private int readBytes(I2CDevice device, byte[] buffer, int byteSize)
    {
        byte[] returnByte = new byte[byteSize];
        int offset = 0;
        int bytesRead = 0;

        try
        {
            bytesRead = device.read(buffer, offset, byteSize);
        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bytesRead;
    }

    /**
     * Write a byte to the given i2c device in the param, does not carry a
     * register address to be read first
     *
     * @param device The device to wrtie byte to
     * @param sendByte The byte to be sent
     */
    private void writeByte(I2CDevice device, byte sendByte)
    {
        try
        {
            device.write(sendByte);

        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Communication.Communication.writeByte(): WRITE GAVE IO-EXCEPTION");
        }
    }

    /**
     * Write a byte[] to the given i2c device in the param, does not carry a
     * register address to be read first
     *
     * @param device The device to wrtie byte to
     * @param sendByte The byte[] to be sent
     */
    private void writeBytes(I2CDevice device, byte cmdAddr, byte[] sendByte)
    {
        try
        {
            device.write(sendByte);

        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Communication.Communication.writeByte(): WRITE GAVE IO-EXCEPTION");
        }
    }

    /**
     * Write byte[] to the specified device with the specified cmd.
     *
     * @param device The I2CDevice to write to
     * @param sendByte The byte[] to send to respective i2c device
     * @param sendAddress The register address for the sent byte[]
     */
    private void writeByteToAddr(I2CDevice device, byte[] sendByte, byte sendAddress)
    {
        try
        {
            device.write(sendAddress, sendByte);

        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Communication.Communication.writeByteToAddr(): WRITE GAVE IO-EXCEPTION");
        }
    }

    private void testCommando()
    {
        byte b = 0b00000001;
        byte b2 = 100;
        Commando comm = new Commando(b);
        int i = 15;
        System.out.println("Setting int value");
        comm.setIntValue(i);
        System.out.println(comm.getIntValue());
        System.out.println("Setting byte value");
        comm.setValue(b2);
        System.out.println(Byte.toString(comm.getByteValue(0)));

    }

    /**
     * Handles the commandos given in parameter. Tasks handled based on Commando
     * subclass.
     *
     * @param cmd The commando to perform
     */
    public void sendCommand(Commando cmd)
    {
        writeCommand(cmd);
        /**
         * COMMANDS TO ARDUINO*
         */
        //Check for move command
        if (cmd instanceof Move)
        {
            doMove(cmd);
        } //Check for acceleration command
        else if (cmd instanceof Acceleration)
        {
            //Do the X-Y movement first and send to the controller
            Acceleration cmdAccl = (Acceleration) cmd;
            if (!(cmdAccl.getElevatorAcclParam().equals(null)))
            {
                this.writeByteToAddr(linearRobot, cmdAccl.getElevatorAcclParam(), cmd.getCmdAddr());
            }
            if (!(cmdAccl.getLinearRobotAcclParam().equals(null)))
            {
                this.writeByteToAddr(linearRobot, cmdAccl.getLinearRobotAcclParam(), cmd.getCmdAddr());
            }

        } //Check for calibrate command and do the tasks   
        else if (cmd instanceof Calibrate)
        {
            doCalibrate(cmd);
        } //Check for suction command
        else if (cmd instanceof Suction)
        {
            //Do the X-Y movement first and send to the controller
            Suction cmdSuction = (Suction) cmd;
            this.writeBytes(linearRobot, cmd.getCmdAddr(), cmd.getValue());
            this.writeBytes(elevatorRobot, cmd.getCmdAddr(), cmd.getValue());
        } //Check for velocity command
        else if (cmd instanceof Velocity)
        {
            //Do the X-Y movement first and send to the controller
            Suction cmdSuction = (Suction) cmd;
            this.writeBytes(linearRobot, cmd.getCmdAddr(), cmd.getValue());
            this.writeBytes(elevatorRobot, cmd.getCmdAddr(), cmd.getValue());
        }

        /**
         * COMMANDS "FROM" ARDUINO*
         */
        /*
        //Check for move command
        if (cmd instanceof StateRequest)
        {
            //Storing of bytes
            byte[] returnByteLinear = null;
            byte[] returnByteElevator = null;
            StateRequest cmdStqry = (StateRequest) cmd;
            //Check if StateRequest is for elevator robot
            if (cmdStqry.forElevatorRobot())
            {
                returnByteElevator = readByteFromAddr(elevatorRobot, cmd.getCmdAddr(), 1);
            }

            //Check if StateRequest is for linear robot
            if (cmdStqry.forLinearRobot())
            {
                returnByteLinear = readByteFromAddr(linearRobot, cmd.getCmdAddr(), 1);
            }
            //Find the retrievend command and preform the State Update
            //updateState(cmdReg.findCommand(returnByteElevator[0]));
            // updateState(cmdReg.findCommand(returnByteLinear[0]));
            //checkState(cmdReg.findCommand(returnByteElevator[0]), cmdReg.findCommand(returnByteLinear[0]));
            //Reset the state request
            //((StateRequest) cmd).reset();
        }
         */
    }

    /**
     * Do the move command as specified
     *
     * @param cmd The command with attached values etc
     */
    public void doMove(Commando cmd)
    {
        //Do the X-Y movement first and send to the controller
        Move cmdMove = (Move) cmd;
        byte[] xyByte = null;
        //Combine the xyByte from the cmd move
        if (cmdMove.getxValue() != null && cmdMove.getyValue() != null)
        {
            xyByte = new byte[cmdMove.getxValue().length + cmdMove.getyValue().length];
            System.arraycopy(cmdMove.getxValue(), 0, xyByte, 0, cmdMove.getxValue().length);
            System.arraycopy(cmdMove.getyValue(), 0, xyByte, cmdMove.getxValue().length, cmdMove.getxValue().length);
        }

        //Write the respective x-y-z values to the respective controllers
        writeByteToAddr(linearRobot, xyByte, cmd.getCmdAddr());

        if (cmdMove.getzValue() != null)
        {
            writeByteToAddr(elevatorRobot, cmdMove.getzValue(), cmd.getCmdAddr());
        }
    }

    private void doCalibrate(Commando cmd)
    {
        writeByte(linearRobot, cmd.getCmdAddr());
        writeByte(elevatorRobot, cmd.getCmdAddr());
    }

    private void writeCommand(Commando cmd)
    {

    }

    /**
     * Write specified bytes to the device
     *
     * @param device Device to send bytes to
     * @param sendBuff Byte[] to send
     * @param byteSize Number of bytes to send
     */
    private void writeBytesWithSize(I2CDevice device, byte[] sendBuff, int byteSize)
    {
        int offset = 0;
        try
        {
            device.write(sendBuff, offset, byteSize);
        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

/*
    /**
     * Set the State true for the respective device in param
     * @param device Device to set state for
     * @param cmd The state
 */
 /*
    private void updateState(I2CDevice device, Commando cmd)
    {
        if(device.equals(linearRobot))
            cmd.setLinearRobot(true);
        
        if(device.equals(elevatorRobot))
            cmd.setElevatorRobot(true);
    }
 */
 /*
    
    WAITING FOR READY TO RECIEVE
        //Keep sending coordinates until they give OK recieved message back
        while (!linearBotOk || !elevatorBotOk)
        {
            //Check the linear and elevator bot are ok
            if (!linearBotOk)
            {
                linearBotOk = readyState(linearRobot);
            }
            if (!elevatorBotOk)
            {
                elevatorBotOk = readyState(elevatorRobot);
            }
        }

        //Send the X-Y Movement
        if (linearBotOk)
        {
            writeByteToAddr(linearRobot, xyByte, cmd.getCmdAddr());
        }

        ///Send the Z movement
        if (elevatorBotOk)

        {
            writeByteToAddr(elevatorRobot, cmdMove.getzValue(), cmd.getCmdAddr());
        }

    
    
    
    
 */
