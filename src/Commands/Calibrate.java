/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Commands.Commando;

/**
 * Command for the I2C device's to do a calibration
 * @author PerEspen
 */
public class Calibrate extends Commando
{
    private byte[] xSteps;
    private byte[] ySteps;
    private byte[] zSteps;
    private final static int defaultByte = 0;

    
    private static final byte COMMAND_ADDRESS = 0x10;
    
    public Calibrate( )
    {
        super(COMMAND_ADDRESS);
        super.setNrOfBytes(defaultByte);
    }


    
    
}
