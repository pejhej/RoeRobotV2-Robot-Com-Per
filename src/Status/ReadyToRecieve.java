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
public class ReadyToRecieve extends Status
{
    
    public ReadyToRecieve(byte statusAddr, int bytes)
    {
        super(statusAddr, bytes);
    }
    
}
