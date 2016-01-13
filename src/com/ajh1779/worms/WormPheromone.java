/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.values.VariableDecimal;

/**
 *
 * @author Arthur
 */
public class WormPheromone {
    /**
     * Creates a new pheromone type with zero quantity with the specified name.
     * @param name 
     */
    public WormPheromone(String name) {
        this(name, 0.0, Double.POSITIVE_INFINITY);
    }
    /**
     * Creates a new pheromone type with zero quantity and the specified name
     * and mean lifetime.
     * @param name
     * @param mean_lifetime 
     */
    public WormPheromone(String name, double mean_lifetime) {
        this(name, 0.0, mean_lifetime);
    }
    /**
     * Creates a new pheromone type with the specified start quantity and the
     * specified name and mean lifetime.
     * @param name
     * @param start
     * @param mean_lifetime 
     */
    public WormPheromone(String name, double start, double mean_lifetime) {
        this.initial = new VariableDecimal("Initial " + name, start);
        this.mean_lifetime = new VariableDecimal(name + " Mean Lifetime", mean_lifetime);
    }
    private final VariableDecimal initial, mean_lifetime;
    private double quantity;
    
    /**
     * Returns the initial amount of pheromone.
     * @return 
     */
    public VariableDecimal getInitial() {
        return initial;
    }
    /**
     * Returns the mean lifetime of the pheromone.
     * @return 
     */
    public VariableDecimal getMeanLifetime() {
        return mean_lifetime;
    }
    
    /**
     * Returns the quantity of pheromone.
     * @return 
     */
    public double getValue() {
        return quantity;
    }
    /**
     * Resets the pheromone to the initial value.
     */
    public void reset() {
        quantity = initial.getValue();
    }
    /**
     * Progresses the pheromone over the specified time, allowing decay.
     * @param delt 
     */
    public void progress(double delt) {
        if(mean_lifetime.getValue() != Double.POSITIVE_INFINITY) {
            quantity *= Math.exp(delt / mean_lifetime.getValue());
        }
    }
    /**
     * Adds the specified amount of pheromone linearly over the specified time.
     * This accounts for the decay of pheromone as it is progressing.
     * @param delt
     * @param rate 
     */
    public void modify(double delt, double rate) {
        if(mean_lifetime.getValue() == Double.POSITIVE_INFINITY) {
            quantity += rate * delt;
        } else {
            double change = rate * delt;
            double life = mean_lifetime.getValue();
            double inv_life = 1.0 / life;
            quantity += (change * life * life) * Math.exp(- inv_life * delt)
                    + change * life * delt - change * life * life;
        }
    }
    
    /**
     * Sets the variables of this pheromone object to the same as the provided
     * pheromone object.
     * @param phero 
     */
    public void set(WormPheromone phero) {
        this.initial.set(phero.initial);
        this.mean_lifetime.set(phero.mean_lifetime);
    }
    
    /**
     * Returns the value of this pheromone as a string.
     * @return 
     */
    @Override
    public String toString() {
        return Double.toString(getValue());
    }
}