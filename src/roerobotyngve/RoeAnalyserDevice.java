/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

import Commands.CalibParam;
import Commands.Calibrate;
import Commands.CloseTray;
import Commands.Move;
import Commands.OpenTray;
import Commands.StateRequest;
import Commands.Stop;
import Commands.Suction;
import I2CCommunication.I2CCommunication;
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
import static com.pi4j.wiringpi.Gpio.delay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import javafx.concurrent.Worker;
import org.junit.rules.Stopwatch;




/**
 * This class represents a robot and all its possible commands and actions. 
 * It can open trays, close trays, move robot to specified x,y,z, 
 * pickup and remove roe from given coordinate.
 * @author Yngve & Per Espen
 */
public class RoeAnalyserDevice {

       public RoeAnalyserDevice(I2CCommunication i2c)
    {
        i2cComm = i2c;
     
    }

  
       
    //Enum for holding the states
    private enum State
    {
        
        Busy(new Busy()),
        Stopped(new Stopped()),
        ReadyToRecieve(new ReadyToRecieve()),
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
        private static final HashMap<Status, State> lookup = new HashMap<Status, State>();

        //Put the states with the accompanied value in the hashmap
        static
        {
            //Create reverse lookup hash map 
            for (State s : State.values())
            {
                lookup.put(s.getStateStatus(), s);
            }
        }
        //Satus address
        private Status status;

        private State(Status status)
        {
            this.status = status;
        }

        public Status getStateStatus()
        {
            return status;
        }

        public static State get(String address)
        {
            //the reverse lookup by simply getting 
            //the value from the lookup HsahMap. 
            return lookup.get(address);
        }
    }
    
    //Stopwatch
    Stopwatch stopwatch;
    private final static int waitTimeMillis = 50;
    
    //Default height for going down to row
    int defaultSuckHeight = 40;
    int defaultHeight = 50;
    
    //The timer for this object
    Timer timer = new Timer();
    
    
      //Holds the current status sent by the roerobot
    Status currentStatus;
    
    
    //I2c communication 
    I2CCommunication i2cComm;
    
    
    
    /**
     * Move method used for moving the end-effector to a specific X,Y,Z coordinat
     *
     * @param coordinat in a global coordinat system.
     */
    public void move(Cordinate cordinat) 
    {
        //Do what necessary form moving the end-effector to a spesific coordinat. 
        //Create the command and set the appropriate values
        Move moveCmd = new Move();
        moveCmd.setIntXValue(cordinat.getxCoord());
        moveCmd.setIntYValue(cordinat.getxCoord());
        moveCmd.setIntZValue(cordinat.getxCoord());
        //Add to the communication sending queue
        i2cComm.addSendQ(moveCmd);
        
    }

    
    /**
     * Open tray will open a tray with a specific number.
     *
     * @param trayNumber is the number of the tray wanted to open.
     * @return False if the tray number do not exist.
     */
    public void openTray(int trayNumber) 
    {
        updateStatus();
         while(!isReady())
        {//wait until device is ready
            stopWatch(waitTimeMillis);
            updateStatus();
        }
        
        // Generate the Move cmd for moving to the tray(nr) openging coordinate.
        // Send cmd. 
        // Check enum for status. 
        // Generate a Lock cmd used for trigg the Lock machanism to close. 
        // Send cmd. 
        // Check enum for status. 
        // Generate the Move cmd for opening the tray completely open
        // Send cmd. 
        // Check enum for status. 
        // Generate a Lock cmd used for trigg the Lock machanism to open.
        // Send cmd. 
        
        OpenTray cmdOpenTray = new OpenTray();
        
        if(Integer.bitCount(trayNumber) >= Short.SIZE)
             cmdOpenTray.setShortValue((short) trayNumber);
        else
        cmdOpenTray.setIntValue(trayNumber);
        
        //Send the close tray commando
        i2cComm.addSendQ(cmdOpenTray);
    }

    
    /**
     * Close Tray will close a tray with a spesific number.
     *
     * @param trayNumber is the nuber of the tray wanted to close
     * @return False if the tray number do not exist.
     */
    public void closeTray(int trayNumber) 
    {
        while(!isReady())
        {//wait until device is ready
            
            
        }
        
        // Generate the Move cmd for moving to the tray(nr) closing coordinate. 
        // Send cmd. 
        // Check enum for status. 
        // Generate a Lock cmd used for trigg the Lock machanism to close. 
        // Send cmd. 
        // Check enum for status. 
        // Generate the Move cmd for closing the tray completely open
        // Send cmd. 
        // heck enum for status. 
        // Generate a Lock cmd used for trigg the Lock machanism to open.
        // Send cmd. 
        CloseTray cmdCloseTray = new CloseTray();
        
        if(Integer.bitCount(trayNumber) >= Short.SIZE)
             cmdCloseTray.setShortValue((short) trayNumber);
        else
        cmdCloseTray.setIntValue(trayNumber);
        
        //Send the close tray commando
        i2cComm.addSendQ(cmdCloseTray);
    }
    
    
    /**
     * Removes roe from all the coordinates given in the Arraylist. 
     * @param coordinates Arraylist of coordinates to be removed from. 
     */
    private void removeRoe(ArrayList<Cordinate> cordinates)
    {
        // 1. Generate a move cmd to the first coordinat.
        // 2. send cmd. 
        // 3. Remove Coordinate form arrayList. 
        // 4. Generate pickUpRoe cmd for removing the roe. 
        // 5. Send cmd.
        // 6. redo form point 1. to the arrayList is empty. 
        Iterator itr = cordinates.iterator();
        
        while(itr.hasNext())
        {
            //Move command
            Move cmdMove = new Move();
            //Get the next coordinate
            Cordinate cord = (Cordinate) itr.next();
            
              while(!isReady())
            {//wait until device is ready
            }
              
            this.move(cord);
            //Check if the current state is what the program wants it to be
            while(!isReady())
                //Update state or just wait until state gets.
            
           pickUpRoe();
        }
    }

    
    /**
     * Calibrate will send a calibration command to the roerobot
     */
    public void calibrate() 
    {
        Calibrate calicmd = new Calibrate();
        i2cComm.addSendQ(calicmd);
        // Generate a Calibration command. 
        // Send cmd. 
        
        long startTime = System.nanoTime();
         long waitTime = 100000;
         
         boolean stopOnce = false;
         //TODO: TESTING EREMEMBER
        while(!isReady())
        {
            //TODO: remove this delay shit
             delay(500);
             updateStatus();
             
             //TODO: REMOVE THIS AFTER TESTING
           /* if(waitTime < (System.nanoTime() - startTime) && !stopOnce)
            {    
                System.out.println("****Stop triggered*****");
           //  this.stopRobot();
             stopOnce = true;
             }
             */
             
        }
        
        //Send calib param command to get calibration parameters
        CalibParam cmdCalibPar = new CalibParam();
        i2cComm.addRecieveQ(cmdCalibPar);
    }
    
    /**
     * Sends a stop Command to the robot
     */
    public void stopRobot()
    {
        Stop stop = new Stop();
        i2cComm.addSendQ(stop);
    }
    
    
    /**
     * Toggle light
     */
    public void toggleLight() 
    {
        //Toggle light
    }
    
    /**
     * Send all the required commands for picking up roe
     * Go down to Z height, and send suction
     */
      private void pickUpRoe()
    {
        
        //Create and send the move cmd
        Move moveDown = new Move();
        moveDown.setIntZValue(defaultSuckHeight);
        
        //Move the robot back up
        Move moveUp = new Move();
        moveUp.setIntZValue(defaultHeight);
        
        //Move the robot down
        i2cComm.addSendQ(moveDown);
        
        
        //Suction
        Suction suck = new Suction();
        
         while(!isReady())
         {    //Update state or just wait until state gets ready
             //Delay
             updateStatus();
         }   
         
         //Send suction command
         i2cComm.addSendQ(suck);
         
         
         //Wait for the Robot to finish before sending more requests to it
         while(!isReady())
         {    
             updateStatus();
         }
         
         //Move the robot up
         i2cComm.addSendQ(moveUp);
     }
    
    
      /**
       * Wait for status to be ready, return true if status is ready, false if not
       * @return Return true if status is ready
       */
    private boolean isReady()
    {
        //Return bool
        boolean ready = false;
        //Check status
        if(currentStatus != null)
        {
        if(currentStatus.getString().equalsIgnoreCase(State.ReadyToRecieve.getStateStatus().getString().toLowerCase()))
            ready = true;
        }
        
        //TODO: ONLY FOR TESTING
        if(i2cComm.returnTriggered())
            ready = true;
        
        return ready;
    }
    
    
     /**
     * Return the number of trays.
     * @return
     */
    public int getNumberOfTrays() {
        // TODO: Fill method
        // Generate cmd for requesting nr of trays in rack from arduino.         
        return 3; // TODO: Return number of trays in rack. 
    }
    
     /**
     * Take a picture at e specefic frame number.
     *
     * @param framNumber
     */
    public void takePicture(int framNumber) {
        // TODO: Fill method
    }
    
    
    /**
     * Send a request for state update
     */
    public void updateStatus()
    {
        StateRequest stateReq = new StateRequest();
        i2cComm.addRecieveQ(stateReq);
    }
 
    
    
    private void stopWatch(long waitMillis)
    {
        long initiatedMillis = System.nanoTime();
        while(initiatedMillis < waitMillis+initiatedMillis);
            
    }
    
    
    //TODO: Only for testing
    public void toggleStatusReady()
    {
        currentStatus = State.ReadyToRecieve.getStateStatus();
    }
    
    public void toggleBusyReady()
    {
        currentStatus = State.Busy.getStateStatus();
    }
}
