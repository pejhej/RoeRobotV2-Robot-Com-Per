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
   private final int nrOfBytes;
   
    public  Status(byte statusAddr, int nrBytes)
            {
                this.StatusAddress = statusAddr;
                this.nrOfBytes = nrBytes;
            }
    

    public byte getStatusAddress()
    {
        return StatusAddress;
    }

    public int getNrOfBytes()
    {
        return nrOfBytes;
    }
        
}
