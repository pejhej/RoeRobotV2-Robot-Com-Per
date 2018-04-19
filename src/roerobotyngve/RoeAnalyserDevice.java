/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

import Commands.CalibParam;
import Commands.Calibrate;
import Commands.Commando;
import Commands.Light;
import Commands.LockGripper;
import Commands.Move;
import Commands.ReleaseGripper;
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
import StatusListener.StatusListener;

import static com.pi4j.wiringpi.Gpio.delay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import javafx.concurrent.Worker;
import javax.swing.text.html.HTMLDocument;
import org.junit.rules.Stopwatch;

/**
 * This class represents a robot and all its possible commands and actions. It
 * can open trays, close trays, move robot to specified x,y,z, pickup and remove
 * roe from given coordinate.
 *
 * @author Yngve & Per Espen
 */
public class RoeAnalyserDevice implements StatusListener
{

    public RoeAnalyserDevice(I2CCommunication i2c)
    {
        i2cComm = i2c;

    }

    

    /**
     * Notification of incoming statuses 
     * 
     * @param status being trigged
     */
    @Override
    public void notifyNewStatus(Status status) 
    {
        
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
    //Timer timer = new Timer();
    //Timer variabales
    private long timerTime = 0;
    private long waitTime = 1000000;

    //Holds the current status sent by the roerobot
    Status currentStatus;

    //I2c communication 
    I2CCommunication i2cComm;

    //Tray
    Tray currentTray;
    TrayRegister trayReg;

    //Flag bool to know if a faulty status has been recieved
    private boolean robotFault = false;

    
    
    
    

    /**
     * Open tray will open a tray with a specific number.
     *
     * @param trayNumber is the number of the tray wanted to open.
     * @return False if the tray number do not exist.
     */
    public boolean openTray(int trayNumber)
    {
    
        
        //Return bool how the task went
        boolean succesful = true;

        //Switch case variables
        int task = 0;
        // the tasks to be completed
        final int moveRobotToHandle = 0, lockGripper = 1, moveOpenTray = 2, releaseGripper = 3, moveToDefault = 4, done = 5;
        
        //Get the tray to work on
         Tray workTray = trayReg.getTray(trayNumber);
         //Check if the tray was retrieved succesfully, else exit the method
         if(workTray.equals(null))
         {
             task = done;
             succesful = false;
         }
        
        //Switch case to do the tasks;
        switch (task)
        {
            //Move robot to the position of the handle
            case moveRobotToHandle:
                //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Move command
                    Move cmdMove = new Move();
                    //Get the coordinates from the handle
                    Coordinate handleCord = workTray.getHandleCoordinate();
                    this.move(workTray.getHandleCoordinate());
                } //Something is faulty, end the task
                else
                {
                    succesful = false; //Set succesful to false
                    task = done;        //End the task
                }

            case lockGripper:
              //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Create the lock grip command
                    LockGripper cmdLockGripper = new LockGripper();
                    //Lock the gripper
                    this.i2cComm.addSendQ(cmdLockGripper);
                } //Something is faulty, end the task
                else
                {
                    succesful = false; //Set succesful to false
                    task = done;        //End the task
                }
                
                  case moveOpenTray:
              //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Send move command to open the tray;
                    move(workTray.getOpenCoord());
                } //Something is faulty, end the task
                else
                {
                    succesful = false; //Set succesful to false
                    task = done;        //End the task
                }
                
            case releaseGripper:
                //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Create the release grip command
                    ReleaseGripper cmdReleaseGrip = new ReleaseGripper();
                    //Send command to release the gripper
                    this.i2cComm.addSendQ(cmdReleaseGrip);
                } //Something is faulty, end the task
                else
                {
                    succesful = false; //Set succesful to false
                    task = done;        //End the task
                }
                
            case moveToDefault:
                  //Move to default after opening tray
                   move(workTray.getDefaultCoord());
                   
            case done:
                break;
        }
        
        //Save this opening tray to the currentTray
        currentTray = workTray;

        return succesful;

    }

    /**
     * Close Tray will close a tray with a specific number.
     *
     * @param trayNumber is the nuber of the tray wanted to close
     * @return False if the tray number do not exist.
     */
    public boolean closeTray(int trayNumber)
    {
        
       //Return bool how the task went
        boolean sucessful = true;

        //Switch case variables
        int task = 0;
        // the tasks to be completed
        final int moveRobotToCloseHandle = 0, lockGripper = 1, moveCloseTray = 2, releaseGripper = 3, moveToDefault = 4, done = 5;
        
        //Get the tray to work on, or use the current tray
         Tray workTray = trayReg.getTray(trayNumber);
         //Check if the tray was retrieved succesfully, else exit the method
         if(workTray.equals(null))
         {
             task = done;
             sucessful = false;
         }
        
        //Switch case to do the tasks;
        switch (task)
        {
            //Move robot to the position of the handle
            case moveRobotToCloseHandle:
                //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Move command
                    Move cmdMove = new Move();
                    //Get the coordinates from the handle
                    Coordinate openHandleCord = workTray.getCloseTrayCoord();
                    this.move(openHandleCord);
                } //Something is faulty, end the task
                else
                {
                    sucessful = false; //Set succesful to false
                    task = done;        //End the task
                }

            case lockGripper:
              //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Create the lock grip command
                    LockGripper cmdLockGripper = new LockGripper();
                    //Lock the gripper
                    this.i2cComm.addSendQ(cmdLockGripper);
                } //Something is faulty, end the task
                else
                {
                    sucessful = false; //Set succesful to false
                    task = done;        //End the task
                }
                
              case moveCloseTray:
              //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Send move command to open the tray;
                    move(workTray.getHandleCoordinate());
                } //Something is faulty, end the task
                else
                {
                    sucessful = false; //Set succesful to false
                    task = done;        //End the task
                }
                
            case releaseGripper:
                //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
                if (robotIsReady(waitTime))
                {
                    //Create the release grip command
                    ReleaseGripper cmdReleaseGrip = new ReleaseGripper();
                    //Send command to release the gripper
                    this.i2cComm.addSendQ(cmdReleaseGrip);
                } //Something is faulty, end the task
                else
                {
                    sucessful = false; //Set succesful to false
                    task = done;        //End the task
                }
                
            case moveToDefault:
                  //Move to default after opening tray
                   move(workTray.getDefaultCoord());
                   
            case done:
                break;
        }
        
        //set current tray to null
        currentTray = null;
        
        //Return the completion bool
        return sucessful;
    }

    /**
     * Removes roe from all the coordinates given in the Arraylist.
     *
     * @param coordinates Arraylist of coordinates to be removed from.
     */
    private boolean removeRoe(ArrayList<Coordinate> cordinates)
    {

        //Return bool if the task was completed or not
        boolean succesful = true;

        Iterator itr = cordinates.iterator();

        while (itr.hasNext() && succesful)
        {
            //Get the next coordinate
            Coordinate cord = (Coordinate) itr.next();

            //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
            if (robotIsReady(waitTime) && succesful)
            {
                //Move command
                Move cmdMove = new Move();
                //Move the robot to the dead roe position
                this.move(cord);
            } //Something is faulty, end the task
            else
            {
                succesful = false;
            }

            //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
            if (robotIsReady(waitTime) && succesful)
            {
                //Pick up the dead roe
                pickUpRoe(currentTray);
            } //Something is faulty, end the task
            else
            {
                succesful = false;
            }
        }

        return succesful;
    }

    /**
     * Calibrate will send a calibration command to the roerobot
     */
    public boolean calibrate()
    {
        boolean succesful = true;
        // Generate a Calibration command. 
        // Send cmd. 
        Calibrate calicmd = new Calibrate();
        i2cComm.addSendQ(calicmd);
                //Wait for the Robot to finish(get in ready to recieve state) before sending more requests to it
        delay(300); //TODO: ONLY FOR TEST
                
        if (robotIsReady(waitTime))
        {
            //Send calib param command to get calibration parameters
            CalibParam cmdCalibPar = new CalibParam();
            i2cComm.addRecieveQ(cmdCalibPar);
        } //Something is faulty, end the task
        else
        {
            succesful = false;
        }

        return succesful;
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
     * Send all the required commands for picking up roe Go down to Z height,
     * and send suction
     */
    private boolean pickUpRoe(Tray thisTray)
    {
        //Return bool for result of task
        boolean succesful = true;

       
        //Switch case variables
        int task = 0;
        // the tasks to be completed
        final int moveDown = 0, suck = 1, moveUp = 2, done = 3;

        //Switch case to do the tasks;
        switch (task)
        {
            
            case moveDown:
                System.out.println("moveDown");
                //Send command if robot becomes ready
                if (robotIsReady(waitTime))
                {
                    this.move(thisTray.getRoePickupZCoord());
                } //Something is faulty, end the task
                else
                {
                    succesful = false;
                    task = done;
                }
            //Move down to the roe pickup height
            //Do the suction task - Check robot is ready, send suction command
            case suck:
                //Send command if robot becomes ready
                if (robotIsReady(waitTime))
                {
                    Suction cmdSuck = new Suction();
                    //Send suction command
                    i2cComm.addSendQ(cmdSuck);
                } //Something is faulty, end task
                else
                {
                    succesful = false;
                    task = done;
                }
            
            //Move the robot up, from the tray
            case moveUp:
                //Send command if robot becomes ready
                if (robotIsReady(waitTime))
                {
                    this.move(thisTray.getDefaultCoord());
                } //Something is faulty, end task
                else
                {
                    succesful = false;
                    task = done;
                }
                
                //The task is done, break
            case done:
                break;
        }
        
        return succesful;
    }

    /**
     * Returns true if robot is in ready state, false if some status is marked
     * as critical
     *
     * @return Returns true if robot is in ready state, false if robot has error
     */
    private boolean robotIsReady(long pollTime)
    {
        boolean robotState = true;
        //Check if robot is ready for new command & no faults are present
        while (!isReady() && !robotFaultyStatus())
        {
            //After a set wait time, update the status
            if (timerHasPassed(pollTime))
            {
                delay(1000);//TODO: ONLY FOR TEST
                updateStatus(); //Send status update request
                resetTimer();   //Reset timer
            }
        }
        //Check if robot has a critical error
        if (robotFaultyStatus())
        {
            robotState = false;
        }

        return robotState;
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
        if (currentStatus != null)
        {
            //Check if status is ready to recieve
            if (currentStatus.getString().equalsIgnoreCase(State.ReadyToRecieve.getStateStatus().getString().toLowerCase()))
            {
                ready = true;
            }
        }
        //Return the decided bool state
        return ready;
    }

    
    /**
     * Return the number of trays.
     *
     * @return
     */
    public int getNumberOfTrays()
    {
        // TODO: Fill method
        // Generate cmd for requesting nr of trays in rack from arduino.         
        return 3; // TODO: Return number of trays in rack. 
    }

    
    /**
     * Move method used for moving the end-effector to a specific X,Y,Z
     * coordinat
     *
     * @param coordinat in a global coordinat system.
     */
    public void move(Coordinate cordinat)
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
     * Take a picture at an specific frame number.
     * Return true if completed, false if frame couldn't be found or robot got faulty status
     * @param frameNumber The number wanted to take picture of
     */
    public boolean takePicture(int frameNumber)
    {
        //Return bool for result of task
        boolean succesful = true;

       
        //Switch case variables
        int task = 0;
        // the tasks to be completed
        final int moveToFrame = 0, takePic = 1, done = 2;

        //Switch case to do the tasks;
        switch (task)
        {
            //Move to the given frame number
            case moveToFrame:
                //Send command if robot becomes ready
                if (robotIsReady(waitTime))
                {
                    //Get the frame coord from the current tray
                    Coordinate frameCord = currentTray.getFrameCoord(frameNumber);
                    //Check if the frame was found
                    if(frameCord != null)
                    {
                        this.move(frameCord);
                    }
                    //Set the completion of this task to fail and exit it
                    else
                    {
                        succesful = false;
                        task = done;
                    }
                    
                } //Something is faulty, end the task
                else
                {
                    succesful = false;
                    task = done;
                }
            //Move down to the roe pickup height
            //Do the suction task - Check robot is ready, send suction command
            case takePic:
                //Send command if robot becomes ready
                if (robotIsReady(waitTime))
                {
                   //Take the pic
                } //Something is faulty, end task
                else
                {
                    succesful = false;
                    task = done;
                }
          
                
                //The task is done, break
            case done:
                break;
        }
        
        return succesful;
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
        while (initiatedMillis < waitMillis + initiatedMillis);

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

    /**
     * Turn light on (send light command with value 1 as payload)
     */
    public void turnOnLight()
    {
        //Create command
        Light cmdLight = new Light();
        //Make the control byte to be sent
        cmdLight.setOn();

        //Return bool for result of task
        boolean succesful = true;

        //Check if robot is ready for new command & no faults are present
        while (!isReady() && !robotFaultyStatus())
        {
            //After a set wait time, update the status
            if (timerHasPassed(waitTime))
            {
                delay(300);
                updateStatus(); //Send status update request
                resetTimer();   //Reset timer
            }
        }
        //Check if robot has a critical error
        if (robotFaultyStatus())
        {
            succesful = false;
        }

        //Send command
        i2cComm.addSendQ(cmdLight);
    }

    /**
     * Turn light off (send light command with value 0 as payload)
     */
    public boolean turnOffLight()
    {
        //Return bool for result of task
        boolean succesful = true;

        //Toggle light
        //Create command
        Light cmdLight = new Light();
        cmdLight.setOff();
        //Check if robot is ready for new command & no faults are present
        while (!isReady() && !robotFaultyStatus())
        {
            //After a set wait time, update the status
            if (timerHasPassed(waitTime))
            {
                updateStatus(); //Send status update request
                resetTimer();   //Reset timer
            }
        }
        //Check if robot has a critical error
        if (robotFaultyStatus())
        {
            succesful = false;
        }

        //Send command
        i2cComm.addSendQ(cmdLight);

        return succesful;
    }

    /**
     * Resets the timer
     */
    private void resetTimer()
    {
        timerTime = System.nanoTime();
    }

    /**
     * Returns true if the timer has passed given nanoseconds;
     *
     * @param waitNanosecs
     * @return Returns true if timer has passed given nanoseconds
     */
    private boolean timerHasPassed(long waitNanosec)
    {
        boolean timerPassed = false;
        //When (nanotime - timertimer) is bigger than wait time, 
        //timer has passed given time
        if (waitNanosec < (System.nanoTime() - timerTime))
        {
            timerPassed = true;
        }
        
        return timerPassed;
    }

    /**
     * Returns true if the the current status is regarded as a fault
     *
     * @return Returns true if the the current status is regarded as a fault
     */
    private boolean robotFaultyStatus()
    {
        boolean returnThis = false;
        if(currentStatus != null)
        {
            returnThis = currentStatus.critical();
        }
        return returnThis;
                
    }
    
    public void testElevatorCMD(Commando cmd)
        {
        i2cComm.addSendQ(cmd);
        }
}
