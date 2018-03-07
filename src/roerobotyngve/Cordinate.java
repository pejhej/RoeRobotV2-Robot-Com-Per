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
public class Cordinate {

    private int xCoord;
    private int yCoord;
    private int zCoord;
    
    
    public Cordinate(int xCoord, int yCoord, int zCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    /**
     * Get x coordinat returns an x coordinat in a global coordinat system
     *
     * @return int representing a x coodrinat in a global coordinat system
     */
    public int getxCoord() {
        return xCoord;
    }

    /**
     * Get y coordinat returns an y coordinat in a global coordinat system
     *
     * @return int representing a y coodrinat in a global coordinat system
     */
    public int getyCoord() {
        return yCoord;
    }

    /**
     * Get z coordinat returns an z coordinat in a global coordinat system
     *
     * @return int representing a z coodrinat in a global coordinat system
     */
    public int getzCoord() {
        return zCoord;
    }

}
