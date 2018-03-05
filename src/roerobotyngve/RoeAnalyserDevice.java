/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

import I2CCommunication.I2CCommunication;
import java.util.ArrayList;



/**
 * This class represents a robot and all its possible commands and actions. 
 * It can open trays, close trays, move robot to specified x,y,z, 
 * pickup and remove roe from given coordinate.
 * @author Yngve & Per Espen
 */
public class RoeAnalyserDevice {
    
    
    I2CCommunication i2cComm;
    
    RoeAnalyserDevice(I2CCommunication i2c)
    {
        i2cComm = i2c;    
    }

    
    
    /**
     * Move method used for moving the end-effector to a specific X,Y,Z coordinat
     *
     * @param coordinat in a global coordinat system.
     */
    public void move(Coodinate coordinat) 
    {
        //Do what necessary form moving the end-effector to a spesific coordinat. 
    }

    
    /**
     * Open tray will open a tray with a specific number.
     *
     * @param trayNumber is the number of the tray wanted to open.
     * @return False if the tray number do not exist.
     */
    public boolean openTray(int trayNumber) 
    {
        
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
        return true;
    }

    
    /**
     * Close Tray will close a tray with a spesific number.
     *
     * @param trayNumber is the nuber of the tray wanted to close
     * @return False if the tray number do not exist.
     */
    public boolean closeTray(int trayNumber) 
    {
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
        return true;
    }
    
    
    /**
     * Removes roe from all the coordinates given in the Arraylist. 
     * @param coordinates Arraylist of coordinates to be removed from. 
     */
    private void removeRoe(ArrayList<Coodinate> coordinates)
    {
        // 1. Generate a move cmd to the first coordinat.
        // 2. send cmd. 
        // 3. Remove Coordinate form arrayList. 
        // 4. Generate pickUpRoe cmd for removing the roe. 
        // 5. Send cmd.
        // 6. redo form point 1. to the arrayList is empty. 
        
    }

    
    /**
     * Calibrate will send a calibration command.
     */
    public void calibrate() 
    {
        // Generate a Calibration command. 
        // Send cmd. 
    }
    
    
    /**
     * 
     */
    public void toggleLight() 
    {
        //Toggle light
    }
    
    
}
