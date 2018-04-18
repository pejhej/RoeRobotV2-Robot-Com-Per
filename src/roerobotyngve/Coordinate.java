/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

/**
 * Representate a coordinat in a global coodrinate system.
 *
 * @author Yngve
 */
public class Coordinate {

    private final int xCoord;
    private final int yCoord;
    private final int zCoord;
    
    
    public Coordinate(int xCoord, int yCoord, int zCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }
    
    public Coordinate(int xCoord, int yCoord)
    {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = 0;
    }

    /**
     * Get x coordinat returns an x coordinat in a global coordinat system
     *
     * @return int representing a x coodrinat in a global coordinat system
     */
    public int getxCoord() {
        return this.xCoord;
    }

    /**
     * Get y coordinat returns an y coordinat in a global coordinat system
     *
     * @return int representing a y coodrinat in a global coordinat system
     */
    public int getyCoord() {
        return this.yCoord;
    }

    /**
     * Get z coordinat returns an z coordinat in a global coordinat system
     *
     * @return int representing a z coodrinat in a global coordinat system
     */
    public int getzCoord() {
        return this.zCoord;
    }
    

}
