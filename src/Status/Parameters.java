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
public class Parameters extends Status
{
         //Status name for this class
    private static final String STATUS = "PARAMETERS";
    
    //ADDRESS For this parameter
    private static final byte COMMAND_ADDRESS = 0x70;
    
    public Parameters(int nrBytes)
    {
        super(COMMAND_ADDRESS, nrBytes, STATUS);
    }
    
}
