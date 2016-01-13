/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms.calculations;

import com.ajh1779.worms.WormSystem;
import com.ajh1779.worms.WormThread;
import com.ajh1779.worms.values.VariableDecimal;

/**
 *
 * @author Arthur
 */
public class WormNumber implements WormCalculation {
    public WormNumber(String name, double number) {
        this.number = new VariableDecimal(name, number);
    }
    private WormNumber(VariableDecimal dec) {
        this.number = dec;
    }
    private final VariableDecimal number;
    @Override
    public double calc(WormSystem system) {
        return number.getValue();
    }
    
    @Override
    public WormNumber copy() {
        return new WormNumber(new VariableDecimal(number));
    }
    
    @Override
    public void addToVariables(WormThread thread) {
        thread.addVariables(number);
    }
    @Override
    public void addToIterators(WormThread thread) {
        thread.addIterators(number);
    }
    @Override
    public void removeFromVariables(WormThread thread) {
        thread.removeVariables(number);
    }
    @Override
    public void removeFromIterators(WormThread thread) {
        thread.removeIterators(number);
    }
}
