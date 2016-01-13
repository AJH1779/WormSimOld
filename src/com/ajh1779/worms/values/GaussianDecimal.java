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
public class GaussianDecimal extends RandomValue<Double> {
    public GaussianDecimal(String name, Double value, Double mean, Double std_dev) {
        super(name, value);
        this.mean = mean;
        this.std_dev = std_dev;
    }
    private Double mean, std_dev;

    @Override
    public void setValue(Double new_value) {
        this.value = new_value;
    }
    
    /**
     * Sets the mean to the new specified value.
     * @param new_mean 
     */
    public void setMean(Double new_mean) {
        mean = new_mean;
    }
    /**
     * Sets the standard deviation to the new specified value.
     * @param new_std_dev 
     */
    public void setStandardDeviation(Double new_std_dev) {
        std_dev = new_std_dev;
    }
    /**
     * Returns the mean of this sampler.
     * @return 
     */
    public Double getMean() {
        return mean;
    }
    /**
     * Returns the standard deviation of this sampler.
     * @return 
     */
    public Double getStandardDeviation() {
        return std_dev;
    }

    /**
     * Sets the value to a randomly selected one from the Gaussian distribution
     * with the contained mean and standard deviation.
     */
    @Override
    public void random() {
        setValue(RNG.nextGaussian() * std_dev + mean);
    }

    @Override
    public GaussianDecimal copy() {
        return new GaussianDecimal(this.getName(), this.value, this.mean, this.std_dev);
    }
}