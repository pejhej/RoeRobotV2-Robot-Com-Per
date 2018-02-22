/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Commands.Commando;

/**
 *
 * @author PerEspen
 */
public class StateRequest extends Commando
{
    private boolean elevatorRobot = false;
    private boolean linearRobot = false;

     private static final byte COMMAND_ADDRESS = 0x30;
    
    public StateRequest()
    {
        super(COMMAND_ADDRESS);
    }
    
    public boolean forElevatorRobot()
    {
        return elevatorRobot;
    }

    public void setElevatorRobot(boolean elevatorRobot)
    {
        this.elevatorRobot = elevatorRobot;
    }

    public boolean forLinearRobot()
    {
        return linearRobot;
    }

    public void setLinearRobot(boolean linearRobot)
    {
        this.linearRobot = linearRobot;
    }
    
    public void reset()
    {
        this.elevatorRobot = false;
        this.linearRobot = false;
    }
    
}
