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

/**
 *
 * @author Arthur
 */
public class ReproduceWormEvent implements WormChange {
    private final WormType next_stage;
    private final WormType egg;
    private WormCalculation mult;

    public ReproduceWormEvent(WormType next_stage, WormType egg, double mult) {
        this.next_stage = next_stage;
        this.egg = egg;
        this.mult = new WormNumber(next_stage.getTypeName() + " to " + egg.getTypeName(), mult);
    }
    
    public void setMultiplier(WormCalculation calc) {
        mult = calc;
    }

    @Override
    public void run(WormGroup group, WormSystem system) {
        group.setType(next_stage);
        system.addGroup(egg, (int) Math.round(group.getNumber() * mult.calc(system)));
    }

    @Override
    public void set(WormChange change_protocol) {
        if(change_protocol instanceof ReproduceWormEvent) {
            ReproduceWormEvent evt = (ReproduceWormEvent) change_protocol;
            
        } else {
            throw new IllegalArgumentException("Provided invalid change protocol!: " + change_protocol.getClass().getSimpleName());
        }
    }

    @Override
    public void addToVariables(WormThread thread) {
        this.mult.addToVariables(thread);
    }

    @Override
    public void addToIterators(WormThread thread) {
        this.mult.addToIterators(thread);
    }
}