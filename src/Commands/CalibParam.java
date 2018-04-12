/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

/**
 *
 * @author PerEspen
 */
public class CalibParam extends Commando
{
    private static final byte COMMAND_ADDRESS = 0x31;
    
    private int defaultBytes = 9;
    
    public CalibParam( )
    {
        super(COMMAND_ADDRESS);
        super.setNrOfBytes(defaultBytes);
        
    }
}
