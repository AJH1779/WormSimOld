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
import com.ajh1779.worms.calculations.WormNumber;
import com.ajh1779.worms.values.VariableDecimal;

/**
 * An event which change a group's worm type to the specified type.
 * @author Arthur
 */
public class DirectWormEvent implements WormChange {
    /**
     * Creates an event which changes a group into the specified type of worm.
     * @param next_stage The type to turn into.
     */
    public DirectWormEvent(WormType next_stage) {
        this(next_stage, 1.0);
    }
    /**
     * Creates an event which changes a group into the specified type of worm
     * and multiplies the number by the specified multiplier.
     * @param next_stage The type to turn into.
     * @param multiplier The multiplier
     */
    public DirectWormEvent(WormType next_stage, double multiplier) {
        this(next_stage, new WormNumber("To " + next_stage.getTypeName(), multiplier));
    }
    /**
     * Creates an event which changes a group into the specified type of worm
     * and multiplies the number by the specified calculation result.
     * @param next_stage The type to turn into.
     * @param multiplier The multiplier calculation
     */
    public DirectWormEvent(WormType next_stage, WormCalculation multiplier) {
        this.next_stage = next_stage;
        this.multiplier = multiplier;
    }
    private WormType next_stage;
    private WormCalculation multiplier;
    
    /**
     * Returns the type of worm that the group will turn into.
     * @return 
     */
    public WormType getNextStage() {
        return next_stage;
    }
    public WormCalculation getMultiplier() {
        return multiplier;
    }
    /**
     * Returns the multiplier for the number of worms.
     * @param system
     * @return 
     */
    public double getMultiplier(WormSystem system) {
        return multiplier.calc(system);
    }
    /**
     * Changes to the type of worm that will be turned into.
     * @param new_next_stage 
     */
    public void setNextStage(WormType new_next_stage) {
        next_stage = new_next_stage;
    }
    /**
     * Changes the multiplier for worms changing.
     * @param new_multiplier 
     */
    public void setMultiplier(WormCalculation new_multiplier) {
        multiplier = new_multiplier;
    }
    
    @Override
    public void run(WormGroup group, WormSystem system) {
        group.setType(next_stage);
        group.setNumber((int)Math.round(group.getNumber() * getMultiplier(system)));
    }

    @Override
    public void set(WormChange change_protocol) {
        if(change_protocol instanceof DirectWormEvent) {
            DirectWormEvent evt = (DirectWormEvent) change_protocol;
            this.multiplier = evt.multiplier.copy();
        } else {
            throw new IllegalArgumentException("Provided invalid change protocol!: " + change_protocol.getClass().getSimpleName());
        }
    }

    @Override
    public void addToVariables(WormThread thread) {
        multiplier.addToVariables(thread);
    }
    @Override
    public void addToIterators(WormThread thread) {
        multiplier.addToIterators(thread);
    }
}
