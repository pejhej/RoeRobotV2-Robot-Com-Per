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
public class SafetySwitchLower extends Status
{
 
      //Status name for this class
    private static final String STATUS = "SAFETY_SWITCH_LOWER";
    //Address for this status
    private static final byte COMMAND_ADDRESS = 0x61;
 
    public SafetySwitchLower( )
    {
        super(COMMAND_ADDRESS, STATUS);
    }
    
     @Override
     public boolean critical()
     {
         return true;
     }
 
     /**
      * Notify listeners of lower safety switch state change
      */
     public void notifyListeners()
     {
         if(this.listeners != null)
         {
             for(StatusListener listener : listeners)
             {
                 listener.notifySafetySwitchLower();
             }
         }
     }
}
