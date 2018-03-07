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
public class Stop extends Commando
{
    //The commando address
      private static final byte COMMAND_ADDRESS = 0x07;
      
    public Stop()
    {
        super(COMMAND_ADDRESS);
    }
}
