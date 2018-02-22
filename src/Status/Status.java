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
    //Address for the status
    private final byte StatusAddress;
   //Number of bytes if other message then address is carried
    private  int nrOfBytes;
    
   //private boolean triggered;
   
    private final String STATUS;
    
    public  Status(byte statusAddr, int nrBytes, String name)
            {
                this.StatusAddress = statusAddr;
                this.nrOfBytes = nrBytes;
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
        
    }
    
     public String getString()
    {
        return this.STATUS;
    }
     
     
     
}
