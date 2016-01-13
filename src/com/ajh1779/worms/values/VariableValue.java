/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.values;

import java.util.Random;

/**
 *
 * @author Arthur
 * @param <T>
 */
public abstract class VariableValue<T> extends RandomValue<T> {
    @SuppressWarnings("OverridableMethodCallInConstructor")
    protected VariableValue(String name, T min, T max, T value) {
        super(name);
        this.min = min;
        this.max = max;
        setValue(value);
    }
    protected T min, max;
    
    /**
     * Returns the minimum allowed value.
     * @return 
     */
    public T getMin() {
        return min;
    }
    /**
     * Returns the maximum allowed value.
     * @return 
     */
    public T getMax() {
        return max;
    }
    /**
     * Sets the current value of the system. Alters the bounds so that the value
     * fits within the bounds.
     * @param new_value 
     */
    @Override
    public abstract void setValue(T new_value);
    /**
     * Sets the minimum of the value. Alters the maximum and value to fit the new
     * minimum.
     * @param new_min 
     */
    public abstract void setMin(T new_min);
    /**
     * Sets the maximum of the value. Alters the minimum and value to fit the new
     * maximum.
     * @param new_max 
     */
    public abstract void setMax(T new_max);
    /**
     * Sets the value to the minimum.
     */
    public void setToMin() {
        setValue(min);
    }
    /**
     * Sets the value to the maximum.
     */
    public void setToMax() {
        setValue(max);
    }
    /**
     * Steps up the value.
     */
    public abstract void stepUp();
    /**
     * Steps down the value.
     */
    public abstract void stepDown();
    /**
     * Returns true if the value will change when it is stepped up.
     * @return 
     */
    public abstract boolean hasStepUp();
    /**
     * Returns true if the value will change when it is stepped down.
     * @return 
     */
    public abstract boolean hasStepDown();
    
    public abstract void randomStep();
    @Override
    public abstract VariableValue copy();
}
