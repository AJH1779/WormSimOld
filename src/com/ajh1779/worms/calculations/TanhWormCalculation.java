/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.calculations;

import com.ajh1779.worms.WormSpecies;
import com.ajh1779.worms.WormSystem;
import com.ajh1779.worms.WormThread;
import com.ajh1779.worms.values.VariableDecimal;

/**
 *
 * @author Arthur
 */
public class TanhWormCalculation implements WormCalculation {
    public TanhWormCalculation(String name, WormSpecies species) {
        this.name = name;
        vars = new VariableDecimal[]{/*
            A = new VariableDecimal(name + " A", 0.9, 1.0, 0.01),
            B = new VariableDecimal(name + " B", 0.0001, 0.0002, 0.00001),
            C = new VariableDecimal(name + " C", 0.6, 0.7, 0.01),
            D = new VariableDecimal(name + " D", 0.05, 0.1, 0.005)*/
            A = new VariableDecimal(name + " A", 0.0, 30000.0, 3000.0),
            B = new VariableDecimal(name + " B", 0.0000001, 0.0005, 0.00005),
            C = new VariableDecimal(name + " C", 0.0, 50000.0, 5000.0),
            D = new VariableDecimal(name + " D", 0.0000001, 0.0005, 0.00005)
        };
        this.species = species;
    }
    private final String name;
    private final VariableDecimal vars[], A, B, C, D;
    private final WormSpecies species;
    
    @Override
    public double calc(WormSystem system) {
        double x = system.getPheromone(species).getValue();
        double y = system.getFood();
        //     0.5 * [tanh2({x - A} * B) + 1.0]
        //(1-(0.5*tanh((x-10000)*0.0002) + 0.5))*(tanh((y-10000)*0.0002))+(0.5 * tanh((x-10000)*0.0002) + 0.5)
        return 1.0 - 0.25 * (tanh2((A.getValue() - x) * B.getValue()) + 1.0)
                 * (tanh2((y - C.getValue()) * D.getValue()) + 1.0);
    }
    
    public static double tanh2(double x) {
        double exp = Math.exp(x);
        return (exp - 1.0) / (exp + 1.0);
    }
    
    @Override
    public TanhWormCalculation copy() {
        TanhWormCalculation calc = new TanhWormCalculation(name, species);
        for(int i = 0; i < vars.length; i++) {
            calc.vars[i].set(this.vars[i]);
        }
        return calc;
    }
    
    @Override
    public void addToVariables(WormThread thread) {
        thread.addVariables(vars);
    }
    @Override
    public void addToIterators(WormThread thread) {
        thread.addIterators(vars);
    }
    @Override
    public void removeFromVariables(WormThread thread) {
        thread.removeVariables(vars);
    }
    @Override
    public void removeFromIterators(WormThread thread) {
        thread.removeIterators(vars);
    }
}