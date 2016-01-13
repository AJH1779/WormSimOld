/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.values.VariableDecimal;
import com.ajh1779.worms.values.VariableValue;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Arthur
 */
public abstract class MonteCarloWormThread extends WormThread {
    public MonteCarloWormThread(MonteCarloWormArray array, WormSystem system) {
        this(array, system, new VariableDecimal("variability", 0.0, 1.0, 0.0001));
    }
    /**
     * A Monte Carlo worm thread.
     * @param array
     * @param system
     * @param variability 
     */
    public MonteCarloWormThread(MonteCarloWormArray array, WormSystem system, VariableDecimal variability) {
        super(system);
        this.array = array;
        this.variability = variability.copy();
    }
    protected final MonteCarloWormArray array;
    protected final ArrayList<VariableValue> prev_variables = new ArrayList<>();
    private double suitability = 0.0, prev_suitability = 0.0;
    private final VariableDecimal variability;
    private boolean final_loop = false;
    
    public void setSuitability(double new_suitability) {
        this.suitability = new_suitability;
    }
    public double getSuitability() {
        return this.suitability;
    }
    
    /**
     * Returns the object representing the variability value.
     * @return 
     */
    public VariableDecimal getVariability() {
        return variability;
    }
    
    /**
     * Resets all of the variables to their initial states, and sets the flag
     * for checking whether all the variables have reached the final step.
     */
    @Override
    protected void resetVariables() {
        final_loop = false;
        variability.setToMax();
        prev_variables.clear();
        variables.stream().forEach((v) -> {
            v.random();
            prev_variables.add(v.copy());
        });
    }
    /**
     * Returns true if the variables should produce another step. This is false
     * if the specific thread is selected for culling due to it being probabilistically
     * unfavourable. It also accounts for creation of new threads through
     * duplication.
     * @return 
     */
    @Override
    protected boolean hasVariablesStep() {
        return variability.hasStepDown() || (final_loop = !final_loop);
    }
    /**
     * Steps the next variable up, so as to iterate through all combinations of
     * variables. Note this is quite slow relative to random sampling towards
     * a desired result.
     */
    @Override
    protected void stepVariables() {
        if(keepVariables()) {
            for(int i = 0; i < variables.size(); i++) {
                prev_variables.get(i).setValue(variables.get(i).getValue());
            }
        } else {
            for(int i = 0; i < variables.size(); i++) {
                variables.get(i).setValue(prev_variables.get(i).getValue());
            }
        }
        if(!final_loop) {
            variables.stream().forEach((v) -> {
                v.randomStep();
            });
        }
    }
    
    /**
     * A check to make sure the variables are not within an out of bounds area
     * combined with a probabilistic test.
     * @return 
     */
    protected boolean keepVariables() {
        // System.out.println(suitability + " vs. " + prev_suitability + " = " + exp);
        if(suitability > prev_suitability ||
                Math.exp((suitability - prev_suitability) / (variability.getValue() * 200))
                >= Math.random()) {
            prev_suitability = suitability;
            variability.stepDown();
            return true;
        } else {
            if(Math.random() < 0.25) variability.stepDown();
            return false;
        }
    }
}