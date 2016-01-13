/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779;

import com.ajh1779.worms.MonteCarloWormArray;
import com.ajh1779.worms.WormGroup;
import com.ajh1779.worms.WormPheromone;
import com.ajh1779.worms.WormSpecies;
import com.ajh1779.worms.WormSpecies.CElegans;
import com.ajh1779.worms.WormSystem;
import com.ajh1779.worms.events.WormEvent;
import com.ajh1779.worms.values.VariableDecimal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Arthur
 */
public class TestWormSystem extends WormSystem {/*
    public TestWormSystem() {
        
    }
    public TestWormSystem(VariableDecimal init_food) {
        super(init_food);
    }

    @Override
    public void create() {
        WormSpecies spec = new CElegans("N2");
        spec.create();
        
        this.addInitialWormEventsAndGroups(new WormGroup(spec.get("egg"), 1));
    }

    @Override
    public TestWormSystem copy() {
        TestWormSystem system = new TestWormSystem(this.getInitialFood().copy());
        
        this.species.forEach((entry) -> {
            system.species.add(entry.copy());
        });
        
        this.initial_parameters.forEach((entry) -> {
            WormEvent evt = entry.copy();
            if(evt instanceof WormGroup) {
                
            }
            system.addInitialWormEventsAndGroups();
        });
        
        system.getInitialTime().set(this.getInitialTime());
        system.getMaxTime().set(this.getMaxTime());
        
        this.pheromones.forEach((key, entry) -> {
            WormPheromone sysent = system.getPheromone(key);
            if(sysent != null) {
                sysent.set(entry);
            } else {
                throw new IllegalStateException("Pheromones present not accounted for!");
            }
        });
        
        return system;
    }

    @Override
    public void customInit() {}
    @Override
    public void customAction() {}

    @Override
    public void customOpenOutput(BufferedWriter out_io) throws IOException {}
    @Override
    public void customOutput(BufferedWriter out_io) throws IOException {}*/
}
