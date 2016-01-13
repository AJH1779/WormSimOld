/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.events.WormChange;
import com.ajh1779.worms.values.VariableDecimal;

/**
 *
 * @author Arthur
 */
public class WormType {
    /**
     * Creates a new type of worm, with an update time give in hours and a name
     * for the type.
     * @param species
     * @param cycle_lifetime The time before an update occurs.
     * @param type_name The name of the type.
     * @param consume_rate
     * @param emit_rate
     */
    public WormType(WormSpecies species, String type_name,
            double cycle_lifetime, double consume_rate, double emit_rate) {
        this.species = species;
        this.type_name = type_name;
        this.cycle_lifetime = new VariableDecimal(type_name + " Cycle Lifetime", cycle_lifetime);
        this.consume_rate = new VariableDecimal(type_name + " Consume Rate", consume_rate);
        this.emit_rate = new VariableDecimal(type_name + " Emit Rate", emit_rate);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Species and Name">
    private final WormSpecies species;
    private final String type_name;
    /**
     * Returns the species of worm this type belongs to.
     * @return
     */
    public WormSpecies getSpecies() {
        return species;
    }
    /**
     * Returns the worm type name.
     * @return
     */
    public String getTypeName() {
        return type_name;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private final VariableDecimal cycle_lifetime;
    private final VariableDecimal consume_rate;
    private final VariableDecimal emit_rate;
    /**
     * Returns the time this type cycle lasts for, before the change protocol is
     * called.
     * @return
     */
    public VariableDecimal getCycleLifetime() {
        return cycle_lifetime;
    }
    /**
     * Returns the rate at which food is consumed in food per worm per hour.
     * @return
     */
    public VariableDecimal getConsumeRate() {
        return consume_rate;
    }
    /**
     * Returns the rate at which pheromone is emitted from this worm type in
     * pheromone per worm per hour.
     * @return
     */
    public VariableDecimal getEmitRate() {
        return emit_rate;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Change Protocol">
    private WormChange change_protocol;
    /**
     * Sets the change protocol. See WormChange
     * @see com.ajh1779.worms.WormChange
     * @param change
     */
    public void setChangeProtocol(WormChange change) {
        change_protocol = change;
    }
    /**
     * Returns the change protocol.
     * @return The change protocol.
     */
    public WormChange getChangeProtocol() {
        return change_protocol;
    }
    //</editor-fold>
    
    public void set(WormType type) {
        this.cycle_lifetime.set(type.cycle_lifetime);
        this.consume_rate.set(type.consume_rate);
        this.emit_rate.set(type.emit_rate);
        this.change_protocol.set(type.change_protocol);
    }
    
    /**
     * Progresses the worm system for this worm type. This consumes food and
     * emits pheromone.
     * @param system The system
     * @param delt The time step
     * @param count The number of worms
     */
    public void progress(WormSystem system, double delt, int count) {
        // Temporary, needs to account for death and things
        // This needs to be modified later.
        system.consumeFood(consume_rate.getValue() * delt * count);
        system.emitPheromone(species, delt, emit_rate.getValue() * count);
    }
    
    @Override
    public String toString() {
        return this.type_name;
    }
}