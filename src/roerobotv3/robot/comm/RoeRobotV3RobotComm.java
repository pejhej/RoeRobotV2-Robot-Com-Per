/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotv3.robot.comm;

import Commands.CalibParam;
import Commands.Calibrate;
import Commands.LockGripper;
import Commands.Move;
import Commands.StateRequest;
import Commands.Stop;
import I2CCommunication.I2CCommunication;
import SerialCommunication.SerialCommunication;
import Status.Busy;
import Status.Parameters;
import Status.ReadyToRecieve;
import static com.pi4j.wiringpi.Gpio.delay;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import roerobotyngve.RoeAnalyserDevice;
import roerobotyngve.Coordinate;

/**
 *
 * @author PerEspen
 */
public class RoeRobotV3RobotComm
{
            /**ALL THE COMMAND ADDRESSES FOR THE DIFFERENT COMMANDS **/
    /*FROM THE JAVA/Communication PROGRAM */
   private static final byte MOVE = 0x05;  
   private static final byte SUCTION = 0x06;  
   private static final byte CALIBRATE = 0x10;  
   private static final byte LIGHT = 0x11;  
   private static final byte VELOCITY = 0x20;  
   private static final byte ACCELERATION = 0x21;  
   private static final byte LOCKGRIPPER = 0x22;  
   private static final byte RELEASEGRIPPER = 0x23;  
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

     SerialCommunication serialComm;
     
     
     public RoeRobotV3RobotComm()
     {
         threadPool = Executors.newScheduledThreadPool(MAX_CLIENT_THREADS);
        // serialComm = new SerialCommunication();
       //  serialComm.start();
     }
     
     public void initRun()
     {
          
          
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
          short zval = 103;
     
        move.setShortXValue(xval);
        move.setShortYValue(yval);
        move.setShortZValue(zval);

         //i2comm.addSendQ(move);
         
         yval = 201;
         xval = 202;
         zval = 203;
         move2.setShortXValue(xval);
        move2.setShortYValue(yval);
         move2.setShortZValue(zval);
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
       
 
     }
     
     
     
     public void roeAnalyserDevTest()
     {
       try
       {
           // RoeAnalyserDevice roeADev = new RoeAnalyserDevice(i2comm);
           //
           
           /* roeADev.updateStatus();
           delay(500);
           roeADev.updateStatus();
           delay(500);
           roeADev.updateStatus();
           */
           //  roeADev.calibrate();
           /*  while(true)
           {
           roeADev.updateStatus();
           delay(200);
           }
           */
           //roeADev.updateStatus();
           /*
           Calibrate calib = new Calibrate();
           delay(500);
           roeADev.testElevatorCMD(calib);
           delay(500);
           roeADev.updateStatus();
           delay(500);
           roeADev.updateStatus();
           roeADev.updateStatus();
           delay(1000);
           Stop stop = new Stop();
           roeADev.testElevatorCMD(stop);
           */
           /*roeADev.updateStatus();
           delay(500);
           roeADev.updateStatus();
           delay(500);
           roeADev.updateStatus();
           /* if(waitTime > (System.nanoTime() - startTime))
           {
           
           }
           */
           /* delay(100);
           System.out.println("SEND STOP");
           roeADev.stopRobot();
           */
           
  
   
           
           String string = "dev1, 99, 100";
           System.out.println("String:");
           System.out.println(string);
           byte[] stringByte =  string.getBytes("UTF-8");
           
           System.out.println("Byte to string:");
           String fromByte = new String(stringByte, "UTF-8");
           System.out.println(fromByte);
           
           
           String[] incommingData = {"dev1", "0x05", "10", "50"};
           System.out.println("INC DATA");
           for(int i=0; i< incommingData.length; ++i)
           {
               System.out.println(incommingData[i]);
           }
           
       
           byte[] byteArray = new byte[incommingData.length-1];
            
            for(int i=1; i< incommingData.length; ++i)
            {
                byte[] tempArr = new byte[50];
                
                byteArray[i-1] = Byte.decode(incommingData[i]);
            }
            
            System.out.println("ByteArray");
            System.out.println(Arrays.toString(byteArray));
              
     
       } catch (UnsupportedEncodingException ex)
       {
           Logger.getLogger(RoeRobotV3RobotComm.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
     
     
     
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       
        RoeRobotV3RobotComm roeb = new RoeRobotV3RobotComm();
        roeb.initRun();
        roeb.roeAnalyserDevTest();
        
       
    }
    
    
    
    
    
    
    
    private void sleeping(long sleepTime)
    {
              try
       {
           // delay(50);
           sleep(50);
       } catch (InterruptedException ex)
       {
           Logger.getLogger(RoeRobotV3RobotComm.class.getName()).log(Level.SEVERE, null, ex);
       }
       
    }
    
}
