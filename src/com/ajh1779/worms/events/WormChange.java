/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.events;

import com.ajh1779.worms.WormGroup;
import com.ajh1779.worms.WormSystem;
import com.ajh1779.worms.WormThread;

/**
 *
 * @author Arthur
 */
public interface WormChange {
    /**
     * An event which modifies the specified group based on the system.
     * 
     * @param group The group to modify.
     * @param system The system the group belongs to.
     */
    public abstract void run(WormGroup group, WormSystem system);

    /**
     * Sets the properties of this object to the specified object, supposing
     * it is of the same class. It does not alter what the 
     * @param change_protocol 
     */
    public void set(WormChange change_protocol);

    /**
     * Adds any variables used in this <code>WormChange</code> object to the
     * thread.
     * @param thread 
     */
    public void addToVariables(WormThread thread);
    
    /**
     * Adds any variables used in this <code>WormChange</code> object to the
     * thread.
     * @param thread 
     */
    public void addToIterators(WormThread thread);
}