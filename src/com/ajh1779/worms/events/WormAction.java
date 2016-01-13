/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.events;

import com.ajh1779.worms.WormSystem;

/**
 *
 * @author Arthur
 */
public interface WormAction {
    /**
     * Performs an action on the provided system.
     * @param sys 
     */
    public void action(WormSystem sys);
}