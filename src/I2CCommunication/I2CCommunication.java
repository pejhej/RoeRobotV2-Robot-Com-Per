/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package I2CCommunication;

import Commands.Commando;
import Commands.Light;
import Commands.Move;
import Commands.StateRequest;
import Status.Status;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.LinkedList;

/**
 * This communication class holds the respectively i2c devices used for the i2c
 * communication. Sending and recieving via i2c should be done via this class.
 * It works as an information relay.
 * Recieve commando and send the appropriate data to the respective controllers.
 *
 * @author PerEspen
 */
public class I2CCommunication implements Runnable
{

    //i2c-dev bus used
    private static final int I2CbusNr = 4;
    private static final byte CONTROLLER_ADDR_ELEVATOR = 0x01;
    private static final byte CONTROLLER_ADDR_LINEARBOT = 0x02;
    

    

    //I2C Bus
    I2CBus i2cbus;
    //Controllers
    I2CDevice linearRobot;
    I2CDevice elevatorRobot;
    
    //
    LinkedList<Commando> sendQeue = new LinkedList<Commando>();
    LinkedList<Status> recieveQeue = new LinkedList<Status>();
    
    public I2CCommunication()
            {
                
                //initiate();
            }
    
    

    
       @Override
    public void run()
    {
        //while(true)
        if(!sendQeue.isEmpty())
        {    //Send the commands in the qeue     
            // Only recieve if something is ent
            if(!recieveQeue.isEmpty())
            //
                
        }
        
    }
    
    
    
    
    public void addSendQ(Commando cmd)
    {
     sendQeue.add(cmd);
    }
    
    public void addRecieveQ(Status stat)
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
        byte[] returnByte = new byte[byteSize];
        int offset = 0;
        try
        {
            device.read(address, returnByte, offset, byteSize);

        } catch (IOException ex)
        {
            Logger.getLogger(I2CCommunication.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return returnByte;
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

   
}
