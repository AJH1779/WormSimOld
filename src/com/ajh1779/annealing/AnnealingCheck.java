/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.annealing;

/**
 *
 * @author Arthur
 * @param <S>
 */
public interface AnnealingCheck<S extends AnnealingState> {
    public double compare(S current, S next);
}
