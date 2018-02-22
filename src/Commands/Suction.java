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
public class Suction extends Commando
{
     private static final byte COMMAND_ADDRESS = 0x06;
    public Suction()
    {
        super(COMMAND_ADDRESS);
    }
    
}
