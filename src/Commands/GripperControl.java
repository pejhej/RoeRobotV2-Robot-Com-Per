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
/**
 *
 * @author PerEspen
 */
public class GripperControl extends Commando
{
     private static final byte COMMAND_ADDRESS = 0x22;

     
    public GripperControl( )
    {
        super(COMMAND_ADDRESS);
    }

    
}
