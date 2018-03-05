/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

/**
 *
 * @author Yngve
 */
public class RoeAnalyser {

     //State enum for the switchcase
    private enum State
    {
        Calibrate,
        Running,
        Waiting;
    }

    
    
    private RoeAnalyserDevice roeAnalyser;
    //State enum
    private State state;
    
    //Flag to remember if the tray is open or not
    private boolean trayOpen;
   //The amount of pictures to be taken
    private int nrOfPicturesToBeTaken;
    private int currentTray;
    
    public void RoeAnalyser() {
        this.roeAnalyser = new RoeAnalyserDevice();
        trayOpen = false;
        currentTray = 1;
    }

    private void cycleCase(State state) {
        switch (state) {
            // Sends calibatrion cmd. 
            case Calibrate:
                // Call on calibrate method in roeAnalyser
                // Call on nrOfTrays from raoAnalyser.                
                this.roeAnalyser.calibrate();
            // Starts the calibration cycle

            case Running:
                //Find number of trays in the rack. 
                int nrOfTrays = roeAnalyser.getNumberOfTrays();
                // For each number in the rack run a roe removal sequense. {
                  
                for (int i = currentTray; i > nrOfTrays; i++) {
                    this.currentTray = i;
                    // Open one tray
                    this.roeAnalyser.openTray(i);  
                    this.trayOpen = true;
                    // Find the number of pictures needed to be taken for covering all coordinates in a tray. 
                    nrOfPicturesToBeTaken = 1;
                    //For each picture needed to be taken (Frames) ...
                    for (int j = 1; j > nrOfPicturesToBeTaken; i++){                     
                        //  Take a picture. 
                        this.roeAnalyser.takePicture(j);
                        //      Find dead roe in picture. 
                        //      Find fastest route for removing roe. 
                        //      Call on methode in RoeAnalyserDevice for removing roes. (arraylist) 
                        // Move to a new frame and rep loop.  
                    }
                    // Close the tray. 
                    this.roeAnalyser.closeTray(i);
                    this.trayOpen = false;
                }
                
                currentTray = 1;
                break;
            case Waiting:
                //Wait for interval
                break;
        }   
    }
    
    
    /**
     * Start the robot
     */
    public void startRobot()
    {
        state = State.Running;
    }

}
