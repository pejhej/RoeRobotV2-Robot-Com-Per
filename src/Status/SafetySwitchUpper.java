/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Status;

import Commands.Commando;

/**
 *
 * @author PerEspen
 */
public class SafetySwitchUpper extends Status
{
    
    public SafetySwitchUpper(byte statusAddr, int bytes)
    {
        super(statusAddr, bytes);
    }
    
}
