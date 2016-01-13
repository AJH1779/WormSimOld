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
public class Stat {
    public static double getMean(int[] values) {
        int total = 0;
        for(int i : values) {
            total += i;
        }
        return (double) total / (double) values.length;
    }
    public static double getMean(double[] values) {
        double total = 0.0;
        for(double d : values) {
            total += d;
        }
        return total / (double) values.length;
    }
    public static double getVariance(int[] values, double mean) {
        double total = 0;
        double invpop = 1.0 / values.length;
        for(int i : values) {
            total += (i - mean) * (i - mean) * invpop;
        }
        return Math.sqrt(total);
    }
    public static double getVariance(double[] values, double mean) {
        double total = 0;
        double invpop = 1.0 / values.length;
        for(double d : values) {
            total += (d - mean) * (d - mean) * invpop;
        }
        return Math.sqrt(total);
    }
    public static double getStdDev(int[] values, double mean) {
        return Math.sqrt(getVariance(values, mean));
    }
    public static double getStdDev(double[] values, double mean) {
        return Math.sqrt(getVariance(values, mean));
    }
    public static double getStdErr(int[] values, double mean) {
        return Math.sqrt(getVariance(values, mean) / values.length);
    }
    public static double getStdErr(double[] values, double mean) {
        return Math.sqrt(getVariance(values, mean) / values.length);
    }
}