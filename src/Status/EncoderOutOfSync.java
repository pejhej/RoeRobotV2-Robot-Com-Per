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
public class EncoderOutOfSync extends Status
{
    
    public EncoderOutOfSync(byte statusAddr, int bytes)
    {
        super(statusAddr, bytes);
    }
    
}
