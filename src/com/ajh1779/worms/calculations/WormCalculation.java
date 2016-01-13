/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.calculations;

import com.ajh1779.worms.WormSystem;
import com.ajh1779.worms.WormThread;

/**
 *
 * @author Arthur
 */
public interface WormCalculation {
    /**
     * Performs a calculation using the provided system as the basis.
     * @param system
     * @return 
     */
    public double calc(WormSystem system);
    /**
     * Creates a copy of this worm calculation. The parameters provided
     * @param params
     * @return 
     */
    public WormCalculation copy();
    
    public void addToVariables(WormThread thread);
    public void addToIterators(WormThread thread);
    public void removeFromVariables(WormThread thread);
    public void removeFromIterators(WormThread thread);
}
