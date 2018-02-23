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
public class EncoderOutOfRange extends Status
{
      //Status name for this class
    private static final String STATUS = "ENCODER_OUT_OF_RANGE";
    //Address for this status
    private static final byte COMMAND_ADDRESS = 0x66;
    
    public EncoderOutOfRange(  )
    {
        super(COMMAND_ADDRESS, STATUS);
    }
    
}
