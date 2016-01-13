/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.events;

import com.ajh1779.worms.WormSystem;

/**
 * Can schedule events for the system at the specified time of the system
 * development.
 * @author Arthur
 */
public abstract class WormEvent implements Comparable {
    /**
     * Creates an event scheduled to run at the specified system time.
     * @param time The time to run at
     */
    public WormEvent(double time) {
        this.act_time = time;
    }
    private double act_time;
    
    /**
     * Sets a new time to run at.
     * @param time 
     */
    public void setTime(double time) {
        this.act_time = time;
    }
    /**
     * Returns the time this will run at.
     * @return 
     */
    public double getTime() {
        return act_time;
    }
    
    @Override
    public int compareTo(Object o) {
        double del = act_time - ((WormEvent) o).act_time;
        if(del < 0.0) {
            return -1;
        } else if (del > 0.0) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * This is the method that is called at the specified time of the system.
     * 
     * @param system 
     */
    public abstract void run(WormSystem system);
    
    /**
     * Returns true if this event should be added back to the event queue in the system.
     * @return 
     */
    public abstract boolean willReoccur();
    /**
     * Provides a copy of this event.
     * @return 
     */
    public abstract WormEvent copy();
}