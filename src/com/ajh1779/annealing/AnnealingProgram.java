/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.annealing;

/**
 *
 * @author Arthur
 */
public final class AnnealingProgram implements Runnable {
    
    private AnnealingState current;
    private AnnealingCheck check;
    private AnnealingProcedure procedure;
    
    @Override
    public void run() {
        procedure.run();
    }
    
    private AnnealingState getNextState(double temp) {
        AnnealingState next = current.neighbour();
        double del = check.compare(current, next);
        if(del <= 0 || Math.random() < Math.exp(del / temp)) {
            return next;
        } else {
            return current;
        }
    }
}
