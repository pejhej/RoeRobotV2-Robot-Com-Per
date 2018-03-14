/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package I2CCommunication;

import Commands.Commando;

/**
 *
 * @author Krisspej
 */
public class I2CCommunicationTest extends Thread{
    

    public I2CCommunicationTest() {
        
    }
    
    
    
       /**
     * Add to the sendqueue, only commands
     *
     * @param cmd Commando to be performed
     */
    public void addSendQ(Commando cmd)
    {
        System.out.print("Added cmd to sQ: ");
        System.out.println(cmd.getCmdAddr());
    }

    /**
     * Added to the recieving queue
     *
     * @param stat
     */
    //TODO: Make changes to this recieving thing
    public void addRecieveQ(Commando stat)
    {
        System.out.print("Added cmd to rQ: ");
        System.out.println(stat.getCmdAddr());
    }
    
    
}
