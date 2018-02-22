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
public class Light extends Commando
{
    
    private static final byte COMMAND_ADDRESS = 0x11;
    
    public Light( )
    {
        super(COMMAND_ADDRESS);
    }
    
}
