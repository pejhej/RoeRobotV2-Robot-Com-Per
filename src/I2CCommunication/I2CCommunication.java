/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package I2CCommunication;
import StatusListener.StatusListener;
import Commands.Acceleration;
import Commands.CalibParam;
import Commands.Calibrate;
import Commands.LockGripper;
import Commands.Commando; 
import Commands.Light;
import Commands.Move;
import Commands.ReleaseGripper;
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
import Status.Parameters;
import Status.ReadyToRecieve;
import Status.SafetySwitchLower;
import Status.SafetySwitchUpper;
import Status.Status;
import Status.Stopped;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Map;
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
public class I2CCommunication extends Thread
{

    /*FROM THE ARDUINO/Communication TO THE JAVA PROGRAM*/
 /*private static final byte BUSY = 0x50;
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
     */
    //i2c-dev bus used
    private static final int I2CbusNr = 4;
    private static final byte CONTROLLER_ADDR_ELEVATOR = 0x05;
    private static final byte CONTROLLER_ADDR_LINEARBOT = 0x03;
    
    // list holding the classes listening to the statuses
    private ArrayList<StatusListener> listenerList;
    
    //I2C Bus
    I2CBus i2cbus;
    //Controllers
    I2CDevice linearRobot;
    I2CDevice elevatorRobot;

    // boolean waitingLinearState;
    // boolean waitingElevatorState;
    Status elevatorState;
    Status linearBotState;
    
    //Only for testing
    boolean readyTriggered = false;

    /**
     * ENUM to hold all the addresses connected to the incomming states of the
     * arduinos
     */
    private enum State
    {
        Busy(new Busy()),
        ReadyToRecieve(new ReadyToRecieve()),
        Stopped(new Stopped()),
        EMC(new EMC()),
        SAFETY_SWITCH_UPPER(new SafetySwitchUpper()),
        SAFETY_SWITCH_LOWER(new SafetySwitchLower()),
        ELEV_LIMIT_TRIGG(new ElevatorLimitTrigg()),
        LINEARBOT_LMIT_TRIGG(new LinearBotLimitTrigged()),
        ENCODER_OUT_OF_SYNC(new EncoderOutOfSync()),
        ENCODER_OUT_OF_RANGE(new EncoderOutOfRange()),
        PARAMETER(new Parameters()),
        FLAG_POS(new FlagPos());

        //Hashmap for lookup
        private static final HashMap<Byte, State> lookup = new HashMap<Byte, State>();

        //Put the states with the accompanied value in the hashmap
        static
        {
            //Create reverse lookup hash map 
            for (State s : State.values())
            {
                lookup.put(s.getStateValue(), s);
            }
        }
        //Satus address
        private Status status;

        private State(Status status)
        {
            this.status = status;
        }

        public byte getStateValue()
        {
            return status.getStatusAddress();
        }

        public static State get(byte address)
        {
            //the reverse lookup by simply getting 
            //the value from the lookup HsahMap. 
            return lookup.get(address);
        }
        public Status getStatus()
        {
            return this.status;
        }
     
    }

    //Lists to keep incomming demands in queue
    LinkedList<Commando> sendQeue;
    LinkedList<Commando> recieveQeue;

    HashMap<Byte, Status> statusMap;
    ArrayList<Byte> statusList;

    public I2CCommunication()
    {
        //Create the recieve send lists
        recieveQeue = new LinkedList<Commando>();
        sendQeue = new LinkedList<Commando>();

        //hashmap for status vs byte value
        // fillStatusMap(statusMap);
        //fillStatusList(statusList);
        elevatorState = null;
        linearBotState = null;
        initiate();
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (!sendQeue.isEmpty())
            {    //Send the commands in the qeue
                sendCommand(sendQeue.pop());
                // Only recieve if something is sent
                //TODO: Check this, currently the incomming recieving qeue only can recieve stateRequest, maybe staterequest should be Status 
                //and thereof the incomming demand can handle all kind of "requests" for different states

            }
            if (!recieveQeue.isEmpty())
            {//read the expecting incomming bytes
                //Find the status to create and put all the values in the status
                //Trigger the status listener
                requestStatus(recieveQeue.pop());

                if (elevatorState != null || linearBotState != null)
                {
                    checkStatesAndTrigger(elevatorState, linearBotState);
                } else
                {
                    System.out.println("BOTH OF THE STATES WERE NULL");
                }
            }
        }

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

        //TODO: Dont think casting is needed here, as makeState, checks what state the incomming message is.
        System.out.print("Request addr: ");
        System.out.println(request.getCmdAddr());

        //Storing of bytes
        byte[] returnByteLinearBot = null;
        byte[] returnByteElevator = null;
        //TODO: Fix this staterequest, should maybe be commando. MAYBE REMOVE THE CHECKING
        //When request commando is staterequest both arduinos should be addressed
            /*
        if (request instanceof StateRequest)
        {
            StateRequest cmdStqry = (StateRequest) request;
            System.out.print("Sending request and waiting for query: ");
            returnByteLinearBot = readByteFromAddr(linearRobot, cmdStqry.getCmdAddr(), cmdStqry.getNrOfBytes());
            returnByteElevator = readByteFromAddr(elevatorRobot, cmdStqry.getCmdAddr(), cmdStqry.getNrOfBytes());
                 

        } else if (request instanceof CalibParam)
        {
            //Cast to get the correct command address
            CalibParam cmdCalibPar = (CalibParam) request;
            System.out.print("Command CalibParam");

            //TODO: Commented out returnbyte elevator;
            returnByteLinearBot = readByteFromAddr(linearRobot, cmdCalibPar.getCmdAddr(), cmdCalibPar.getNrOfBytes());
            returnByteElevator = readByteFromAddr(elevatorRobot,cmdCalibPar.getCmdAddr() , cmdCalibPar.getNrOfBytes());

        } //If nothing of the Request commands were recognised, just send a general request
        */
        if(request != null)
        {
//TODO: FOR TESTING            returnByteLinearBot = readByteFromAddr(linearRobot, request.getCmdAddr(), request.getNrOfBytes());
            returnByteElevator = readByteFromAddr(elevatorRobot, request.getCmdAddr() , request.getNrOfBytes());
        }

        /***Making the states and putting the payload inside the status message***/
        if (returnByteLinearBot != null)
        {
            System.out.print("1st byte from returnByteLinearBot:");
            System.out.println(returnByteLinearBot[0]);
            System.out.print("Making linear bot state");
            linearBotState = makeState(returnByteLinearBot);
            
            
            
            //Put the value read from the i2c comm to the desired state
            //Removes first byte because that is address ant not value
            byte[] valueArr = Arrays.copyOfRange(returnByteLinearBot, 1, returnByteLinearBot.length);
            //Checking if there is any values to put in the status
            if(valueArr.length != 0 && linearBotState != null)
            { 
                System.out.print(linearBotState.getString());
                linearBotState.putValue(valueArr);
            }
            //Check state and print, mainly for testing
            if(linearBotState != null)
            {
                System.out.print("Made state: ");
                  System.out.print(linearBotState.getString());
            }
        }
        //Check if anything has been recieved from the elevator
        if (returnByteElevator != null)
        {
             System.out.print("1st byte from returnByteElevatorBot:");
            System.out.println(returnByteElevator[0]);
            System.out.print("Making elevator bot state");
            elevatorState = makeState(returnByteElevator);
            System.out.print("Made state: ");
            
            
                //Copy what is only the values
             byte[] valueArr = Arrays.copyOfRange(returnByteElevator, 1, returnByteElevator.length);
            //Checking if there is any values to put in the status
            if(valueArr.length != 0 && elevatorState != null)
            {
                System.out.print(elevatorState.getString());
                elevatorState.putValue(valueArr);
            }
            //Check state and print, mainly for testing
            if(elevatorState!= null)
            {
                System.out.print("Made state: ");
                  System.out.print(elevatorState.getString());
            }
        }

        //Find the retrievend command and preform the State Update
        //updateState(cmdReg.findCommand(returnByteElevator[0]));
        // updateState(cmdReg.findCommand(returnByteLinear[0]));
        // checkState(cmdReg.findCommand(returnByteElevator[0]), cmdReg.findCommand(returnByteLinear[0]));
        //Reset the state request
        //((StateRequest) cmd).reset();
    }

    /**
     * Check for the readytorecieve state and trigger if both are ready Else
     * trigger the states which are not ready to recieve
     *
     * @param elevatorState The elevator State
     * @param linearBotState The linearbot state
     */
    private void checkStatesAndTrigger(Status elevatorState, Status linearBotState)
    {
        //Safeguarding against null-pointer
        if (elevatorState != null && linearBotState != null)
        {
            
            //System.out.println(elevatorState.equals(State.ReadyToRecieve));
            if((Byte.compare(elevatorState.getStatusAddress(), State.ReadyToRecieve.getStateValue()) == 0) && (Byte.compare(linearBotState.getStatusAddress(), State.ReadyToRecieve.getStateValue()) == 0))
            {
                
                //TODO: Trigger ready to recieve
                System.out.println("ReadyToRecieve triggered");
                //TODO: REMOVE WHEN IMPLEMENTING NOTIFY! ONLY FOR TESTING!!
                readyTriggered = true;
                
            } 
            if (Byte.compare(elevatorState.getStatusAddress(), State.ReadyToRecieve.getStateValue()) != 0)
            {
                //TODO: Trigger this state / send notify
                System.out.println("Elevator state triggered");
                //readyTriggered = false;
                byte[] val = new byte[2];
                elevatorState.trigger(val);
                //TEST
                //readyTriggered = true;
            } 
            if (Byte.compare(linearBotState.getStatusAddress(), State.ReadyToRecieve.getStateValue()) != 0)
            {
                //TODO: Trigger this state
                System.out.println("LinearBotState triggered");
              //  readyTriggered = false;
                
                 byte[] val = new byte[2];
                linearBotState.trigger(val);
                //TEST
               // readyTriggered = true;
            }
        }
        System.out.println("Checking done");
    }

    /**
     * Make a state from the given statebyte[]
     *
     * @param stateByte Statebyte to create state from
     * @return Returns the created state, else null!
     */
    //TODO: MAKE ENUM TO DECIDE WHICH STATUS HAS BEEN SENT FROM ARDUINO
    public Status makeState(byte[] stateByte)
    {
        byte cmdAddr = stateByte[0];
        Status returnState = null;
        System.out.println("State addr: ");
        System.out.print(cmdAddr);

        State state = State.get(cmdAddr);

        System.out.println("Value:");

        //Nullpointer check
        if (state != null)
        {
            System.out.println(state.getStateValue());
        
            
          //   System.out.println("State not recognised!!!");
            Status status = state.getStatus();
            returnState = status.returnNew();
            
            
            // add listeners to the new state
            for(StatusListener listener : this.listenerList)
            {
                returnState.addListener(listener);
            }
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
    public void addRecieveQ(Commando stat)
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
            //TODO: REMOVE WHILE TESTING
          //  linearRobot = i2cbus.getDevice(CONTROLLER_ADDR_LINEARBOT);

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
     * @param byteSize Size of the return buffer byte
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
         System.out.print("BytesRead: ");
         System.out.print(bytesRead);
         System.out.print(", ");
         System.out.print(returnByte[0]);
         
        byte[] modifiedReturnByte = null;
        //create array with exact amount of bytes
        if(bytesRead > 0)
        { modifiedReturnByte = resizeArray(returnByte, (byte) -1);
        }
        
        return modifiedReturnByte;
    }

    /**
     * Resize an array with only carrying information, -1 is considered as not
     * valuable information.
     *
     * @param inputArr
     * @return Return an resized array
     */
    private byte[] resizeArray(byte[] inputArr, byte resizeOption)
    {
        int length = inputArr.length;
        int cnt = 0;
        //Find the actual length of the array

        for (int i = 0; i < length; ++i)
        {
            if (Byte.compare(inputArr[i], resizeOption) != 0)
            {
                ++cnt;
            }

        }
        //Create the new byte[]
        byte[] returnByte = new byte[cnt];
        //Copy the wanted values
        System.arraycopy(inputArr, 0, returnByte, 0, cnt);
        //Return the resized byte[]
        return returnByte;
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
     * Write byte[] to the specified device with the specified cmd.
     *
     * @param device The I2CDevice to write to
     * @param sendByte The byte[] to send to respective i2c device
     * @param sendAddress The register address for the sent byte[]
     */
    private void writeByteToAddr(I2CDevice device, byte sendByte, byte sendAddress)
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

    /**
     * Write a byte[] to the given i2c device in the param, does not carry a
     * register address to be read first
     *
     * @param device The device to wrtie byte to
     * @param sendByte The byte[] to be sent
     */
    private void writeBytes(I2CDevice device, byte[] sendByte)
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
     * Write a byte[] to the given i2c device in the param, does not carry a
     * register address to be read first
     *
     * @param device The device to wrtie byte to
     * @param sendByte The byte[] to be sent
     */
    private void writeBytesToAddr(I2CDevice device, byte cmdAddr, byte[] sendByte)
    {
        try
        {
            device.write(cmdAddr, sendByte);

        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Communication.Communication.writeByte(): WRITE GAVE IO-EXCEPTION");
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

    }

    /**
     * Handles the commandos given in parameter. Tasks handled based on Commando
     * subclass.
     *
     * @param cmd The commando to perform
     */
    public void sendCommand(Commando cmd)
    {
        System.out.print("Commando address: ");
        System.out.println(cmd.getCmdAddr());

        /**
         * COMMANDS TO ARDUINO*
         */
        /**
         * *Checking all the possible commands**
         */
        //Check for move command
        if (cmd instanceof Move)
        {
            doMove(cmd);
        } //Check for acceleration command
        else if (cmd instanceof Acceleration)
        {
            //Cast and send the Acceleration parameters
            Acceleration cmdAccl = (Acceleration) cmd;
            if (!(cmdAccl.getElevatorAcclParam().equals(null)))
            {
                this.writeBytesToAddr(elevatorRobot, cmd.getCmdAddr(), cmdAccl.getElevatorAcclParam());
            }
            if (!(cmdAccl.getLinearRobotAcclParam().equals(null)))
            {
                this.writeBytesToAddr(linearRobot, cmd.getCmdAddr(), cmdAccl.getLinearRobotAcclParam());
            }

        } //Check for calibrate command and do the tasks   
        //Send the calibrate command
        else if (cmd instanceof Calibrate)
        {
//TODO: remove after test            writeByte(linearRobot, cmd.getCmdAddr());
            writeByte(elevatorRobot, cmd.getCmdAddr());
        } //Check for suction command
        else if (cmd instanceof Suction)
        {
            //Do the suction
            Suction cmdSuction = (Suction) cmd;
            this.writeBytesToAddr(linearRobot, cmd.getCmdAddr(), cmd.getValue());
            this.writeBytesToAddr(elevatorRobot, cmd.getCmdAddr(), cmd.getValue());
        } //Check for velocity command
        else if (cmd instanceof Velocity)
        {
            //Send the new velocity params
            Suction cmdSuction = (Suction) cmd;
            this.writeBytesToAddr(linearRobot, cmd.getCmdAddr(), cmd.getValue());
            this.writeBytesToAddr(elevatorRobot, cmd.getCmdAddr(), cmd.getValue());
        } else if (cmd instanceof ReleaseGripper)
        {
            //Control the gripper
            ReleaseGripper cmdOpenTray = (ReleaseGripper) cmd;
            this.writeBytesToAddr(linearRobot, cmdOpenTray.getCmdAddr(), cmdOpenTray.getValue());
            this.writeBytesToAddr(elevatorRobot, cmdOpenTray.getCmdAddr(), cmdOpenTray.getValue());
        }
         else if (cmd instanceof LockGripper)
        {
            //Control the gripper
            LockGripper cmdCloseTray = (LockGripper) cmd;
            this.writeBytesToAddr(linearRobot, cmdCloseTray.getCmdAddr(), cmdCloseTray.getValue());
            this.writeBytesToAddr(elevatorRobot, cmdCloseTray.getCmdAddr(), cmdCloseTray.getValue());
        }
        else if (cmd instanceof Light)
        {
            //Turn light on/off
            Light cmdLight = (Light) cmd;
            this.writeBytesToAddr(linearRobot, cmdLight.getCmdAddr(), cmdLight.getValue());
            this.writeBytesToAddr(elevatorRobot, cmdLight.getCmdAddr(), cmdLight.getValue());
        } else if (cmd instanceof CalibParam)
        {
            CalibParam cmdCalPar = (CalibParam) cmd;
            this.writeByte(linearRobot, cmdCalPar.getCmdAddr());
            this.writeByte(elevatorRobot, cmdCalPar.getCmdAddr());
        } else
        {
            //TODO: Maybe throw exception?
            System.out.println("THE COMMAND WAS NOT RECOGNISED, BUT SENT");
            if(cmd.getValue() != null)
            {
            this.writeBytesToAddr(linearRobot, cmd.getCmdAddr(), cmd.getValue());
            this.writeBytesToAddr(elevatorRobot, cmd.getCmdAddr(), cmd.getValue()); 
            }
            else
            {
                 writeByte(linearRobot, cmd.getCmdAddr());
                 writeByte(elevatorRobot, cmd.getCmdAddr());
            }
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
     * @param cmd The command with attached values
     */
    public void doMove(Commando cmd)
    {
        //Do the X-Y movement first and send to the controller
        Move cmdMove = (Move) cmd;
        byte[] xyByte = new byte[cmdMove.getxValue().length + cmdMove.getyValue().length];
        //Combine the xyByte from the cmd move
        if ((cmdMove.getxValue() != null) && (cmdMove.getyValue() != null))
        {
            /*
            //xyByte = new byte[cmd.getNrOfBytes() + cmd.getNrOfBytes()];
            System.arraycopy(cmdMove.getxValue(), 0, xyByte, 0, cmdMove.getxValue().length);
            System.arraycopy(cmdMove.getyValue(), 0, xyByte, cmdMove.getxValue().length, cmdMove.getxValue().length);

            //Make new byte to send to store the byte[] length in the first byte
            byte[] sendByte = new byte[xyByte.length + 2];
            sendByte[0] = cmd.getCmdAddr();
            sendByte[1] = (byte) ((byte) cmdMove.getxValue().length + cmdMove.getyValue().length);
            System.arraycopy(xyByte, 0, sendByte, 2, sendByte[1]);
            
            System.out.println("Sending do move command to linearbot");
            //Write the bytes with the desired address
            writeBytes(linearRobot, sendByte);
            */
            
            writeBytes(linearRobot, cmdMove.makeCompleteXYByte());
        }
        
        //Z value should be written to elevator robot
        
        if (cmdMove.getzValue() != null)
        {
            /*
            byte[] sendByte = new byte[xyByte.length + 2];
            sendByte[0] = cmd.getCmdAddr();
            sendByte[1] = (byte) cmdMove.getzValue().length;
            
            System.arraycopy(cmdMove.getzValue(), 0, sendByte, 2, cmdMove.getzValue().length);
            
              System.out.println("Sending do move command to elevatorbot");
            writeBytes(elevatorRobot, sendByte);
            */
           
            writeBytes(elevatorRobot, cmdMove.makeCompleteZByte());
        }
        
        System.out.println("Sending done");
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
    
    
    
    /**
     * ONLY FOR TESTING
     * @return 
     */
    public boolean returnTriggered()
    {
        return readyTriggered;
    }

    
    /**
     * Add class as listener to statuses.
     * listener needs to implement StatusListener interface
     * @param listener to add as listener to statuses
     */
    public void addListener(StatusListener listener)
    {
        this.listenerList.add(listener);
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
