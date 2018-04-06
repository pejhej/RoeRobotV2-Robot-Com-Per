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
public class ReadyToRecieve extends Status
{
      //Status name for this class
    private static final String STATUS = "READY";
    //Address for this status
    private static final byte COMMAND_ADDRESS = 0x51;
    
        public ReadyToRecieve( )
    {
        super(COMMAND_ADDRESS , STATUS);
    }
        
    /**
      * Notify listeners of ready to recieve 
      */
     public void notifyListeners()
     {
         if(this.listeners != null)
         {
             for(StatusListener listener : listeners)
             {
                 listener.notifyReadyToRecieve();
             }
         }
     }
}
