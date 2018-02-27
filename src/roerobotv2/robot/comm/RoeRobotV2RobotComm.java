/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotv2.robot.comm;

import Commands.CalibParam;
import Commands.Calibrate;
import Commands.Move;
import Commands.StateRequest;
import I2CCommunication.I2CCommunication;
import Status.Busy;
import Status.Parameters;
import Status.ReadyToRecieve;
import static com.pi4j.wiringpi.Gpio.delay;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author PerEspen
 */
public class RoeRobotV2RobotComm
{
            /**ALL THE COMMAND ADDRESSES FOR THE DIFFERENT COMMANDS **/
    /*FROM THE JAVA/Communication PROGRAM */
   private static final byte MOVE = 0x05;  
   private static final byte SUCTION = 0x06;  
   private static final byte CALIBRATE = 0x10;  
   private static final byte LIGHT = 0x11;  
   private static final byte VELOCITY = 0x20;  
   private static final byte ACCELERATION = 0x21;  
   private static final byte GRIPPERCONTROL = 0x22;  
   private static final byte STATEREQUEST = 0x30;
   private static final byte CALIB_PARAM = 0x31;
   
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
   private static final byte PARAMETERS = 0x70;  
   private static final byte FLAG_POS = 0x71;  
   private static final int MAX_CLIENT_THREADS = 20;
     private ScheduledExecutorService threadPool;

     
     
     
     public RoeRobotV2RobotComm()
     {
        // threadPool = Executors.newScheduledThreadPool(MAX_CLIENT_THREADS);
     }
     
     public void initRun()
     {
          I2CCommunication i2comm = new I2CCommunication();
          i2comm.start();
          //threadPool.execute(i2comm); 
          
        Move move = new Move();
        Move move2 = new Move();
        Move move3 = new Move();
        Move move4 = new Move();
        Move move5 = new Move();
        Move move6 = new Move();
        
        Calibrate calib = new Calibrate();
        Calibrate calib2 = new Calibrate();
        
        /*byte[] xval = new byte[1];
                xval[0] = 20;
                byte[] yval = new byte[1];
                yval[0] = 10;
         */
           move.setNrOfBytes(Short.BYTES);
           move2.setNrOfBytes(Short.BYTES);
           move3.setNrOfBytes(Short.BYTES);
           move4.setNrOfBytes(Short.BYTES);
           move5.setNrOfBytes(Short.BYTES);
           move6.setNrOfBytes(Short.BYTES);
       
           short xval = 101;
        short yval = 102;
     
        move.setShortXValue(xval);
        move.setShortYValue(yval);

         //i2comm.addSendQ(move);
         
         yval = 201;
         xval = 202;
         move2.setShortXValue(xval);
        move2.setShortYValue(yval);
       // i2comm.addSendQ(move2);

         yval = 301;
         xval = 302;
         move3.setShortXValue(xval);
        move3.setShortYValue(yval);
      //  i2comm.addSendQ(move3);
        
             yval = 401;
         xval = 402;
         move4.setShortXValue(xval);
        move4.setShortYValue(yval);
     //   i2comm.addSendQ(move4);
        
        
        yval = 501;
         xval = 502;
        /* byte[] vals = new byte[3];
         vals[0] = (byte)1;
         vals[1] = (byte)2;
         vals[2] = (byte)3;
         move5.setValue(vals);
         byte[] retVal = move5.getValue();
         byte retSize = retVal[0];
         System.out.println(retSize);
        for(int i=1; i<retSize; ++i)
            System.out.println(retVal);
         */
        
       // i2comm.addSendQ(move);
      //  delay(2000);
        //i2comm.addSendQ(calib);
        
       /* i2comm.addSendQ(move2);
        i2comm.addSendQ(move3);
        i2comm.addSendQ(move4);
        i2comm.addSendQ(move5);
        */
       
       StateRequest strq = new StateRequest();
       StateRequest strq1 = new StateRequest();
       StateRequest strq2 = new StateRequest();
       StateRequest strq3 = new StateRequest();
         System.out.println("Sending request");
         
         CalibParam calibparam = new CalibParam();
         
     //  i2comm.addRecieveQ(strq);
      // i2comm.addSendQ(move);
//       i2comm.addSendQ(move2);
//       i2comm.addSendQ(move3);
         System.out.println(strq.getCmdAddr());       
        //i2comm.addRecieveQ(strq);   
        //i2comm.addRecieveQ(calibparam);  
      //  delay(2000);
        
        
        Parameters param = new Parameters();
        
       
          
           System.out.println("Move");
           
        i2comm.addSendQ(move);
        // delay(50);
         System.out.println("StateRequest1, should be READY");
         i2comm.addRecieveQ(strq1); 
         // delay(50);
          System.out.println("Calib");
         i2comm.addSendQ(calib);
        // delay(50);
       
        System.out.println("Move2");
         i2comm.addSendQ(move2);
        // delay(50);
         System.out.println("StateRequest2, should be BUSY");
         i2comm.addRecieveQ(strq2);
       //  delay(50);
        
         System.out.println("Calib2");
         i2comm.addSendQ(calib);
         // delay(50);
        
        //     System.out.println("StateRequest3, should be READY");
        // i2comm.addRecieveQ(strq3);
       //  delay(3000);
         System.out.println("Ask for calib params");
         i2comm.addRecieveQ(calibparam);
         
         i2comm.addSendQ(move2);
         i2comm.addSendQ(move3);
         i2comm.addSendQ(move4);
//         System.out.println("Sending Move");
//         i2comm.addSendQ(move5);
//         delay(1000);
        // System.out.println("Sending request");
         
//        delay(1000);
//         i2comm.addSendQ(move);
//         i2comm.addSendQ(move);
//         i2comm.addSendQ(move);
//         yval = 245;
//         xval = 88;
//         move.setShortXValue(xval);
//        move.setShortYValue(yval);
//        
//         i2comm.addSendQ(move);
//          i2comm.addSendQ(move);
//     }
     
/*
    Busy busyStatus = new Busy(1);
         ReadyToRecieve rdy = new ReadyToRecieve(1);
    byte[] stateByte = new byte[1];
    stateByte[0] = busyStatus.getStatusAddress(); 
   
    i2comm.makeState(stateByte);
     stateByte[0] = rdy.getStatusAddress(); 
    i2comm.makeState(stateByte);
*/
     }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       
        RoeRobotV2RobotComm roeb = new RoeRobotV2RobotComm();
        roeb.initRun();
        
       
    }
    
}
