/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Status;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Status message sent from arduinos. Each object holds unique address.
 * 
 * @author PerEspen
 */
public class Status 
{
    //Address for the status
    private final byte StatusAddress;
    
    private boolean triggered = false;
   //Number of bytes if other message then address is carried
    private  int nrOfBytes;
    
    
    private byte[] value;
    
   //private boolean triggered;
   
    private final String STATUS;
    
    public  Status(byte statusAddr, String name)
            {
                this.StatusAddress = statusAddr;
                this.STATUS = name;
            }
    

    public byte getStatusAddress()
    {
        return StatusAddress;
    }

    public int getNrOfBytes()
    {
        return nrOfBytes;
    }
    
    //TODO: OVERRIDE AND ADD IN THE CALIB PARAM.
    /**
     * Put the byte values where they are supposed to be. 
     * Should be overided in classes with multiple byte storage instead of only trigger bool
     * 
     * @param val The given byte value 
     */
    public void putValue(byte[] val)
    {
        this.value = val;
    }
    
      public byte[] getValue()
    {
        return this.value;
    }
    
    
     public String getString()
    {
        return this.STATUS;
    }
     
     /**
      * Trigger the status, set Status bool high or low depending on input val
      * @param val Inputted value
      */
     public void trigger(byte[] val)
     {
         if(val[0] > 0)
            this.triggered = true;
         else
             this.triggered = false;
     }
     
     /**
      * Return a new Status instance of the object calling it
      * @return 
      */
     public Status returnNew()
     {
         Status returnstat = null;
        try
        {
            returnstat = this.getClass().newInstance();
        } catch (InstantiationException ex)
        {
            Logger.getLogger(Status.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            Logger.getLogger(Status.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return returnstat;
     }
     
     //TODO: Override in subclasses
     /**
      * Returns true if this status is considered as critical for function
      * @return 
      */
     public boolean critical()
     {
         return false;
     }
   
}
