/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Status;



/**
 *
 * @author PerEspen
 */
public class EMC extends Status
{
      //Status name for this class
    private static final String STATUS = "EMC"; 
    //Address for this status
    private static final byte COMMAND_ADDRESS = 0x60;
    
    public EMC( )
    {
        super(COMMAND_ADDRESS, STATUS);
    }
    
}
