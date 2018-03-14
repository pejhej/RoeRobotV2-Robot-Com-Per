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
public class FlagPos extends Status
{
      //Status name for this class
    private static final String STATUS = "FLAG_POS";
    //Address for this status
    private static final byte COMMAND_ADDRESS = 0x71;
    
    
    public FlagPos(  )
    {
        super(COMMAND_ADDRESS, STATUS);
    }
 
    
    
}
