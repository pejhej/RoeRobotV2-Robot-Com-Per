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
public class LinearBotLimitTrigged extends Status
{
      //Status name for this class
    private static final String STATUS = "LINEAR_BOT_LIMIT_TRIGGED";
    //Address for this status
    private static final byte COMMAND_ADDRESS = 0x64;
    
    public LinearBotLimitTrigged( )
    {
        super(COMMAND_ADDRESS , STATUS);
    }
    
}
