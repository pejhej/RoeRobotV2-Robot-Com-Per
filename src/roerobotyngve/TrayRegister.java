/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Rack register class contains an list of trays in a rack.
 *
 * @author Yngve
 */
public class TrayRegister {

    private final ArrayList<Tray> trayRegister;

    public TrayRegister() {
        this.trayRegister = new ArrayList<>();
    }

    /**
     * Adds tray to the rack register
     *
     * @param tray
     */
    public void addToRegister(Tray tray) {
        this.trayRegister.add(tray);
    }

    /**
     * Get number of trays returns the number of trays in the rack.
     *
     * @return int representing number of trays in the rack.
     */
    public int getNumberOfTrays() {
        return this.trayRegister.size();
    }
    /**
     * Return register iterator
     * @return 
     */
    public Iterator getRegisterIterator() {
        return this.trayRegister.iterator();
    }
    
    /**
     * Return the tray based on its Number
     * @param trayNr The tray number to the tray
     * @return Returns the tray with the given tray parameter, null if nothing found
     */
    public Tray getTray(int trayNr)
    {
        Tray returnTray = null;
        Iterator itr = (Iterator) this.getRegisterIterator();
        //Flag bool to exit loop
        boolean found = false;
        //Iterate over the trays
        while(itr.hasNext() && !found)
        {
            Tray itrTray = (Tray) itr.next();
            
            //If the tray nr parameter corresponds with the
            if(Integer.compare(itrTray.getTrayNr(), trayNr) == 0)
            {   
                returnTray = itrTray;
                found = true;
            }
        }
        
        return returnTray;
    }
}
