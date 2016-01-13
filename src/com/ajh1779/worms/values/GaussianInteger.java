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
public class GaussianInteger extends RandomValue<Integer> {
    public GaussianInteger(String name, Integer value, double mean, double std_dev) {
        super(name, value);
        this.mean = mean;
        this.std_dev = std_dev;
    }
    private double mean, std_dev;

    @Override
    public void setValue(Integer new_value) {
        this.value = new_value;
    }
    
    /**
     * Sets the mean to the new specified value.
     * @param new_mean 
     */
    public void setMean(double new_mean) {
        mean = new_mean;
    }
    /**
     * Sets the standard deviation to the new specified value.
     * @param new_std_dev 
     */
    public void setStandardDeviation(double new_std_dev) {
        std_dev = new_std_dev;
    }
    /**
     * Returns the mean of this sampler.
     * @return 
     */
    public double getMean() {
        return mean;
    }
    /**
     * Returns the standard deviation of this sampler.
     * @return 
     */
    public double getStandardDeviation() {
        return std_dev;
    }

    /**
     * Sets the value to a randomly selected one from the Gaussian distribution
     * with the contained mean and standard deviation.
     */
    @Override
    public void random() {
        setValue((int) Math.round(RNG.nextGaussian() * std_dev + mean));
    }

    @Override
    public GaussianInteger copy() {
        return new GaussianInteger(this.getName(), this.value, this.mean, this.std_dev);
    }
}
