/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.values;

import java.util.Random;

/**
 * An object holding a value that can be randomised.
 * @author Arthur
 * @param <T>
 */
public abstract class RandomValue<T> {
    public static final Random RNG = new Random();
    
    /**
     * Creates a new random value with the specified name.
     * @param name 
     */
    protected RandomValue(String name) {
        this.name = name;
    }
    /**
     * Creates a new random value with the specified name and value.
     * @param name
     * @param value 
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    protected RandomValue(String name, T value) {
        this.name = name;
        setValue(value);
    }
    private final String name;
    /**
     * The value of this object.
     */
    protected T value;
    
    /**
     * Returns the current value.
     * @return 
     */
    public T getValue() {
        return value;
    }
    /**
     * Sets the current value of the system.
     * @param new_value 
     */
    public abstract void setValue(T new_value);
    /**
     * Sets the value to a random value with the specified 
     */
    public abstract void random();
    
    /**
     * Returns a copy of this.
     * @return 
     */
    public abstract RandomValue copy();
    
    /**
     * Returns the name of this value.
     * @return 
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the name of this value with the actual value of this object in
     * the format "name:value".
     * @return 
     */
    @Override
    public String toString() {
        return name + ":" + value.toString();
    }
}
