/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Commands.Commando;

/**
 *
 * @author PerEspen
 */

public class Move extends Commando
{

  //The values for X, Z and Y movement
    private byte[] xValue;
    private byte[] yValue;
    private byte[] zValue;
    public Move(byte cmdAddress)
    {
        super(cmdAddress);
        xValue = null;
        yValue = null;
        zValue = null;
    }
    
    
     public byte[] getxValue()
    {
        return xValue;
    }

    public void setxValue(byte[] xValue)
    {
        this.xValue = xValue;
    }

    public byte[] getyValue()
    {
        return yValue;
    }

    public void setyValue(byte[] yValue)
    {
        this.yValue = yValue;
    }

    public byte[] getzValue()
    {
        return zValue;
    }

    public void setzValue(byte[] zValue)
    {
        this.zValue = zValue;
    }
    
    
}
