/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobotyngve;

import java.util.ArrayList;

/**
 * Rack register class contains an list of trays in a rack.
 *
 * @author Yngve
 */
public class TrayRegiser {

    private final ArrayList<Tray> trayRegister;

    public TrayRegiser() {
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

    public ArrayList<Tray> getRegisterIterator() {
        return this.trayRegister;
    }
}
