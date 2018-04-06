/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

import java.util.ArrayList;

/**
 * Class represents a tray. The tray has a width, hight and a depth. The tray
 * will also know where it is plased in a coordinate system by knowing its upper
 * and lower posistion.
 *
 * @author Yngve
 */
public class Tray{
    //Number for this tray
    private final int nr;
    private final int handleOffset = 0;
    private final int width = 10; // defined in mm 
    private final int depth = 10; // defined in mm 
    private final int distUpperLowerPos = 20; // Distanse form the upper position to the lowest point in the tray
    private final int upperPos = 30; // defined in mm parallel to the Z-axis 
    private final int lowerPos = 40; // defined in mm parallel to the Z-axis
    
    private final int TotalCameraPositions = 6;  //Total amount of camera positions
    
    private int nrOfRemovedRoe;
    private int flagPosZ;
    
    //Coordinate for diff positions related to the tray
    //Coord for the handle when tray is close
   private Coordinate getHandleCoord;
   //Default pos for roebot
    private Coordinate defaultPosCoord;
    //Coordinate for opening the tray - from holding tray handle to open
    private Coordinate openTrayCoord;
    //Coordinate for Z coordinate(down to tray roe level)
    private Coordinate pickupRoeZCoord;
    
    //Different camerapossez
    /*private Coordinate cameraPos1;
    private Coordinate cameraPos2;
    private Coordinate cameraPos3;
    private Coordinate cameraPos4;
    private Coordinate cameraPos5;
    private Coordinate cameraPos6;
    */
    private ArrayList<Coordinate> cameraPositions;

    public Tray(int nr, int flagposZ) 
    {
        this.flagPosZ = flagposZ;
        this.nr = nr;
        
        this.cameraPositions = new ArrayList<Coordinate>();
    }

    /**
     * Get upper position returns the upper limit position of the tray defined
     * in mm from the bottom of a global coordinat system
     *
     * @return
     */
    public int getUpperPos() {
        return upperPos;
    }

    /**
     * Get lower position returns the lower limit position of the tray defined
     * in mm from the bottom of a global coordinat system
     *
     * @return
     */
    public int getLowerPos() {
        return lowerPos;
    }

    /**
     * Get distanse between upper and lower position
     *
     * @return int with lower positon.
     */
    public int getDistUpperLowerPos() {
        return distUpperLowerPos;
    }

    /**
     * Increase the number of removed dead roe in the tray.
     *
     * @param removedRoe is the number of dead roe witch has been removed.
     */
    public void increaseNrOfRemovedRoe(int removedRoe) {
        this.nrOfRemovedRoe = this.nrOfRemovedRoe + removedRoe;
    }

    /**
     * Get number of removed roe returns the total number of dead roe witch has
     * been removed from the tray.
     *
     * @return number of removed roe.
     */
    public int getNrOfRemovedRoe() {
        return nrOfRemovedRoe;
    }

    /**
     * Get width returns the width of the tray
     *
     * @return width of tray
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get depth returns the width of the tray
     *
     * @return depth of tray
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Return this trays number
     * @return The number of this tray
     */
    public int getTrayNr()
    {
        return this.nr;
    }
    
    /**
     * Coordinates for the handle to this tray
     * @return Return coordinate for the handle of this tray
     */
    public Coordinate getHandleCoordinate()
    {
        return this.getHandleCoordinate();
    }
    
    
      /**
     * Return the Z Coordinates for the handle to this tray
     * @return Return the Z coordinate for the handle of this tray
     */
    public Coordinate getZHandleCoord()
    {
        Coordinate zCord = new Coordinate(0,0, this.getHandleCoordinate().getzCoord());
        
        return zCord;
    }
    /**
     * Return the coords for pulling the tray to open position
     * @return Return the coords for opening the tray
     */
    public Coordinate getOpenCoord()
    {
       return this.openTrayCoord;
    }
    
        /**
     * Return the coords for opening the tray
     * @return Return the coords for opening the tray
     */
    public Coordinate getDefaultCoord()
    {
       return this.defaultPosCoord;
    }
    
    /**
     * Return the z coord where roe should be pickuped up
     * @return Return the z coord where roe should be pickuped up
     */
    public Coordinate getRoePickupZCoord()
    {
        return this.pickupRoeZCoord;
    }
    
    /**
     * Returns the coord for grabbing the handle to close the tray
     * @return  Returns the coord for grabbing the handle to close the tray
     */
    public Coordinate getCloseTrayCoord()
    {
     Coordinate returnCord = new Coordinate(this.getOpenCoord().getxCoord()+2, this.getOpenCoord().getyCoord(), this.getOpenCoord().getzCoord());
     return returnCord;
    }
    
    /**
     * Return the coordinate for the wanted frame
     * @param nr Number for the fram wanted
     * @return Return the coordinate for the frame corresponding with the param famre number
     */
    public Coordinate getFrameCoord(int nr)
    {
        Coordinate returnCoord = null;
        //Check if the coordinate is in the array
        if(nr <= this.cameraPositions.size())
            returnCoord = this.cameraPositions.get(nr);
       
       return returnCoord;
    }
    
    
    private void addCameraPos(Coordinate camPos)
    {
        this.cameraPositions.add(camPos);
    }
    
  
}
