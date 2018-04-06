/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Status;

import StatusListener.StatusListener;

/**
 *
 * @author PerEspen
 */
public class Busy extends Status
{
      //Status name for this class
    private static final String STATUS = "BUSY";
    
    private static final byte COMMAND_ADDRESS = 0x50;
    
    /**
     *
     */ 
    public Busy()
    {
        super(COMMAND_ADDRESS, STATUS);
    }
    
    
    /**
     * Notify listeners on busy
     */
    public void notifyListeners()
    {
        if(this.listeners != null)
        {
            for(StatusListener listener : listeners)
            {
                listener.notifyBusy();
            }
        }
    }
}
