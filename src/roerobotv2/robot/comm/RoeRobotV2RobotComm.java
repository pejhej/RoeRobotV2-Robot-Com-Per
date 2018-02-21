/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotv2.robot.comm;

import Commands.Move;
import I2CCommunication.I2CCommunication;
import static com.pi4j.wiringpi.Gpio.delay;

/**
 *
 * @author PerEspen
 */
public class RoeRobotV2RobotComm
{
            /**ALL THE COMMAND ADDRESSES FOR THE DIFFERENT COMMANDS **/
    /*FROM THE JAVA/Communication PROGRAM */
   private static final byte MOVE = 0x01;  
   private static final byte SUCTION = 0x02;  
   private static final byte CALIBRATE = 0x10;  
   private static final byte LIGHT = 0x11;  
   private static final byte VELOCITY = 0x20;  
   private static final byte ACCELERATION = 0x21;  
   private static final byte GRIPPERCONTROL = 0x22;  
   private static final byte STATEREQUEST = 0x30;
   
   /*FROM THE ARDUINO/Communication TO THE JAVA PROGRAM*/
   private static final byte BUSY = 0x50;  
   private static final byte READY_TO_RECIEVE = 0x51;  
   private static final byte EMC = 0x60;  
   private static final byte UPPER_SAFETY_SWITCH = 0x61;  
   private static final byte LOWER_SAFETY_SWITCH = 0x62;  
   private static final byte ELEV_LIMIT_TRIGG = 0x63;  
   private static final byte LINEARBOT_LMIT_TRIGG = 0x64;
   private static final byte ENCODER_OUT_OF_SYNC = 0x65;  
   private static final byte ENCODER_OUT_OF_RANGE = 0x66;
   private static final byte CALIB_PARAM = 0x70;  
   private static final byte FLAG_POS = 0x71;  

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        I2CCommunication i2comm = new I2CCommunication();
        Move move = new Move(MOVE);
        i2comm.run();
        delay(1000);
        i2comm.addSendQ(move);
    }
    
}
