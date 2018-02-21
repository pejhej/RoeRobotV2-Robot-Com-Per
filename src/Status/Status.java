/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Status;

/**
 * Status message sent from arduinos. Each object holds unique address.
 * 
 * @author PerEspen
 */
public class Status 
{
    private final byte StatusAddress;
   private  int nrOfBytes;
   private boolean triggered;
   
    public  Status(byte statusAddr, int nrBytes)
            {
                this.StatusAddress = statusAddr;
                this.nrOfBytes = nrBytes;
                triggered = false;
            }
    

    public byte getStatusAddress()
    {
        return StatusAddress;
    }

    public int getNrOfBytes()
    {
        return nrOfBytes;
    }
    
    /**
     * Put the byte values where they are supposed to be. 
     * Should be overided in classes with multiple byte storage instead of only trigger bool
     * @param val The given byte value 
     */
    public void putValue(byte[] val)
    {
        if(val[1] != 0)
            triggered = true;
    
    }
}
