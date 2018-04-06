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
public class Stopped extends Status
{
      //Status name for this class
    private static final String STATUS = "STOPPED";
    
    private static final byte COMMAND_ADDRESS = 0x52;
    
    public Stopped( )
    {
        super(COMMAND_ADDRESS, STATUS);
    }
    
    /**
      * Notify listeners of stopped status 
      */
     public void notifyListeners()
     {
         if(this.listeners != null)
         {
             for(StatusListener listener : listeners)
             {
                 listener.notifyStopped();
             }
         }
     }
    
}