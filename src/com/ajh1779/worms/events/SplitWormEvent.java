/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.events;

import com.ajh1779.worms.WormGroup;
import com.ajh1779.worms.WormSystem;
import com.ajh1779.worms.WormThread;
import com.ajh1779.worms.WormType;
import com.ajh1779.worms.calculations.WormCalculation;

/**
 * An event which splits the population of a group.
 * @author Arthur
 */
public class SplitWormEvent implements WormChange {
    public SplitWormEvent(WormType T1_type, WormType T2_type) {
        this.T1_type = T1_type;
        this.T2_type = T2_type;
    }
    private WormType T1_type, T2_type;
    private WormCalculation T2_Fraction;
    
    /**
     * Sets the worm types for the 
     * @param T1_type
     * @param T2_type 
     */
    public void setWormTypes(WormType T1_type, WormType T2_type) {
        this.T1_type = T1_type;
        this.T2_type = T2_type;
    }
    
    /**
     * Sets the calculation that determines the fraction of the group which
     * becomes the second type.
     * @param calc 
     */
    public void setCalculation(WormCalculation calc) {
        this.T2_Fraction = calc;
    }
    /**
     * Calculates the fraction which take the second type. If no calculation
     * has been provided, none take the second type.
     * @param system
     * @return 
     */
    public final double calcT2Fraction(WormSystem system) {
        if(T2_Fraction == null) {
            return 0.0;
        } else {
            return Math.max(Math.min(T2_Fraction.calc(system), 1.0), 0.0);
        }
    }
    
    @Override
    public void run(WormGroup group, WormSystem system) {
        int T2_number = (int) Math.rint(group.getNumber() * calcT2Fraction(system));
        int T1_number = group.getNumber() - T2_number;

        group.setType(T1_type);
        group.setNumber(T1_number);
        
        system.addGroup(T2_type, T2_number);
    }

    @Override
    public void set(WormChange change_protocol) {
        if(change_protocol instanceof SplitWormEvent) {
            SplitWormEvent evt = (SplitWormEvent) change_protocol;
            this.T2_Fraction = evt.T2_Fraction.copy();
        } else {
            throw new IllegalArgumentException("Provided invalid change protocol!: " + change_protocol.getClass().getSimpleName());
        }
    }

    @Override
    public void addToVariables(WormThread thread) {
        this.T2_Fraction.addToVariables(thread);
    }

    @Override
    public void addToIterators(WormThread thread) {
        this.T2_Fraction.addToIterators(thread);
    }
}