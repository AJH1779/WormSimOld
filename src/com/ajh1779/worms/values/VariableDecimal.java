/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.values;

/**
 *
 * @author Arthur
 */
public class VariableDecimal extends VariableValue<Double> {
    /**
     * Creates a new variable value with the specified name and value.
     * The max and min are both set to the provided value.
     * @param name
     * @param value 
     */
    public VariableDecimal(String name, Double value) {
        super(name, value, value, value);
    }
    /**
     * Creates a new variable with the specified name, minimum, maximum, and
     * step. The value is set to the minimum to begin.
     * @param name
     * @param min
     * @param max
     * @param step 
     */
    public VariableDecimal(String name, Double min, Double max, Double step) {
        this(name, min, max, step, step * 0.001);
    }
    /**
     * Creates a new variable with the specified name, minimum, maximum, and
     * step. The value is set to the minimum to begin.
     * @param name
     * @param min
     * @param max
     * @param step 
     * @param tolerance 
     */
    public VariableDecimal(String name, Double min, Double max, Double step,
            Double tolerance) {
        super(name, min, max, min);
        this.step = step;
        this.tolerance = tolerance;
    }
    /**
     * Creates a new variable with the specified name, minimum, maximum, and
     * step. The value is set to the minimum to begin.
     * @param name
     * @param min
     * @param max
     * @param value
     * @param step 
     * @param tolerance 
     */
    public VariableDecimal(String name, Double min, Double max, Double value,
            Double step, Double tolerance) {
        super(name, min, max, value);
        this.step = step;
        this.tolerance = tolerance;
    }
    /**
     * Creates a new variable with the same properties as the provided variable.
     * @param dec 
     */
    public VariableDecimal(VariableDecimal dec) {
        this(dec.getName(), dec.min, dec.max, dec.value, dec.step, dec.tolerance);
    }
    private Double step = 1.0, tolerance = 0.0005;
    
    /**
     * Returns the step amount.
     * @return 
     */
    public Double getStep() {
        return step;
    }
    /**
     * Returns the tolerance on detecting if there is a next step to take.
     * @return 
     */
    public Double getTolerance() {
        return tolerance;
    }
    /**
     * Sets the minimum, maximum, and step value.
     * @param min
     * @param max
     * @param step 
     */
    public void set(Double min, Double max, Double step) {
        this.value = this.min = min;
        this.max = max;
        this.step = step;
    }
    /**
     * Sets the minimum, maximum, and step value.
     * @param min
     * @param max
     * @param step 
     */
    public void set(Double min, Double max, Double step, Double tolerance) {
        this.value = this.min = min;
        this.max = max;
        this.step = step;
        this.tolerance = tolerance;
    }
    /**
     * 
     * @param vars 
     */
    public void set(VariableDecimal vars) {
        set(vars.getMin(), vars.getMax(), vars.getStep(), vars.getTolerance());
        setValue(vars.getValue());
    }
    /**
     * Sets the current value of the system. Alters the bounds so that the value
     * fits within the bounds.
     * @param new_value 
     */
    @Override
    public void setValue(Double new_value) {
        this.value = new_value;
        if(min > value) {
            min = value;
        }
        if(max < value) {
            max = value;
        }
    }
    /**
     * Sets the minimum of the value. Alters the maximum and value to fit the new
     * minimum.
     * @param new_min 
     */
    @Override
    public void setMin(Double new_min) {
        this.min = new_min;
        if(min > max) {
            max = min;
        }
        if(value < min) {
            value = min;
        }
    }
    /**
     * Sets the maximum of the value. Alters the minimum and value to fit the new
     * maximum.
     * @param new_max 
     */
    @Override
    public void setMax(Double new_max) {
        this.max = new_max;
        if(max < min) {
            min = max;
        }
        if(value > max) {
            value = max;
        }
    }
    /**
     * Sets the new step value.
     * @param step 
     */
    public void setStep(Double step) {
        this.step = step;
    }
    
    /**
     * Steps up the value.
     */
    @Override
    public void stepUp() {
        this.value += step;
        if(value > max) {
            value = max;
        }
    }
    /**
     * Steps down the value.
     */
    @Override
    public void stepDown() {
        this.value -= step;
        if(value < min) {
            value = min;
        }
    }
    /**
     * Returns true if the value will change when it is stepped up.
     * @return 
     */
    @Override
    public boolean hasStepUp() {
        return !(value >= max - tolerance);
    }
    /**
     * Returns true if the value will change when it is stepped down.
     * @return 
     */
    @Override
    public boolean hasStepDown() {
        return !(value <= min + tolerance);
    }
    
    /**
     * Sets the value to a random value between the maximum and minimum.
     */
    @Override
    public void random() {
        this.value = RNG.nextDouble() * (max - min) + min;
    }
    /**
     * Moves the value according to the Gaussian distribution, using the step
     * as the standard deviation.
     */
    @Override
    public void randomStep() {
        this.value += RNG.nextGaussian() * this.step;
        
        if(value < min) {
            value = min;
        } else if(value > max) {
            value = max;
        }
    }
    
    @Override
    public VariableDecimal copy() {
        return new VariableDecimal(this.getName(), this.min, this.max, this.value,
                this.step, this.tolerance);
    }
}