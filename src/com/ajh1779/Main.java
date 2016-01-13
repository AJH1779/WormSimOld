/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779;

import com.ajh1779.worms.MonteCarloWormArray;
import com.ajh1779.worms.MonteCarloWormThread;
import com.ajh1779.worms.WormGroup;
import com.ajh1779.worms.WormSpecies;
import com.ajh1779.worms.WormSystem;
import com.ajh1779.worms.values.GaussianDecimal;
import com.ajh1779.worms.values.RandomValue;
import com.ajh1779.worms.values.Stat;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Arthur
 */
public class Main {
    public static void main(String[] args) {
        // MonteCarloWormArray array = new MonteCarloWormArray(Main::make, 6);
        // array.run();
        // System.exit(0);
        MonteCarloWormThread thread = make();
        thread.run();
        thread.enableSystemOutput();
        thread.runSystem();
    }
    public static MonteCarloWormThread make() {
        RandomValue<Integer> amount = null;
        
        WormSpecies species = new WormSpecies.CElegans("N2");
        species.create();
        
        WormSystem system = new WormSystem(new GaussianDecimal("Initial Food",
                360000.0, 360000.0, 36000.0));
        WormGroup init_group = new WormGroup(species.get("egg"),
                amount == null ? 1 : amount.getValue());
        system.addInitialWormEventsAndGroups(init_group);
        
        MonteCarloWormThread thread = new MonteCarloWormThread(null, system) {
            @Override
            protected void preLoopAction() {
                if(amount != null) {
                    amount.random();
                    init_group.setNumber(amount.getValue());
                }
            }
            @Override
            protected boolean hasIterationStep() {
                return this.getCurrentIterationCount() < max_iters;
            }
            @Override
            protected void iterAction() {
                int i = this.getCurrentIterationCount();
                pop_list[i] = system.getNumberOf("");
                dauer_list[i] = system.getNumberOf("dauer");
                time_list[i] = system.getTime();
                dauer_prop[i] = (double) dauer_list[i] / (double) pop_list[i];
            }
            private final int max_iters = 100;
            private final int[] pop_list = new int[max_iters],
                    dauer_list = new int[max_iters];
            private final double[] time_list = new double[max_iters],
                    dauer_prop = new double[max_iters];
            private double pop_mean, dauer_mean, time_mean,
                    pop_stddev, dauer_stddev, time_stddev;
            private static final double DAUER_VALUE = 5.0, DIFF_COST = 0.001;
            @Override
            protected void postLoopPreOutputAction() {
                pop_mean = Stat.getMean(pop_list);
                dauer_mean = Stat.getMean(dauer_list);
                time_mean = Stat.getMean(time_list);
                pop_stddev = Stat.getStdDev(pop_list, pop_mean);
                dauer_stddev = Stat.getStdDev(dauer_list, dauer_mean);
                time_stddev = Stat.getStdDev(time_list, time_mean);

                setSuitability(dauer_mean * DAUER_VALUE + pop_mean);
            }
            @Override
            public void customOpenOutput(BufferedWriter out_io) throws IOException {
                out_io.write("\tMean Pop\tStd.Dev. Pop"
                        + "\tMean Dauer\tStd.Dev. Dauer"
                        + "\tMean Time\tStd.Dev. Time"
                        + "\tSuitability");
            }

            @Override
            public void customOutput(BufferedWriter out_io) throws IOException {
                out_io.write("\t" + pop_mean + "\t" + pop_stddev
                        + "\t" + dauer_mean + "\t" + dauer_stddev
                        + "\t" + time_mean + "\t" + time_stddev
                        + "\t" + getSuitability());
            }

            @Override
            protected void preOutputInit() {}
            @Override
            protected void postOutputInit() {}
            @Override
            protected void postLoopPostOutputAction() {}
        };
        
        system.disableOutput();
        thread.enableOutput();
        
        species.get("L1").getChangeProtocol().addToVariables(thread);
        species.get("L2d").getChangeProtocol().addToVariables(thread);
        thread.addIterators(system.getInitialFood());
        
        return thread;
    }
    //<editor-fold defaultstate="collapsed" desc="Outdated Starts">
    /*
    public static void main(String[] args) {
    test21();
    }
    public static void test22() {
    CElegans type = new CElegans("N2");
    
    VariableDecimal[] changeL1 = new VariableDecimal[]{
    new VariableDecimal("T=57", 0.0, 1.0, 0.002),
    new VariableDecimal("T=144", 0.0, 1.0, 0.002),
    new VariableDecimal("T=151", 0.0, 1.0, 0.002),
    new VariableDecimal("T>END", 0.0, 1.0, 0.002)
    }, changeL2 = new VariableDecimal[]{
    new VariableDecimal("T=73", 0.0, 1.0, 0.002),
    new VariableDecimal("T=160", 0.0, 1.0, 0.002),
    new VariableDecimal("T=167", 0.0, 1.0, 0.002),
    new VariableDecimal("T>END", 0.0, 1.0, 0.002)
    };
    
    ((SplitWormEvent) type.get("L1").getChangeProtocol()).setCalculation((WormSystem system) -> {
    if (system.getTime() < 140.) { // Accounts for first switch
    return changeL1[0].getValue();
    } else if (system.getTime() < 150.) { // Accounts for the second switch L2 path
    return changeL1[1].getValue();
    } else if (system.getTime() < 160.) { // Accounts for the second switch L2d path
    return changeL1[2].getValue();
    } else {
    return changeL1[3].getValue();
    }
    });
    ((SplitWormEvent) type.get("L2d").getChangeProtocol()).setCalculation((WormSystem system) -> {
    if (system.getTime() < 155.) { // Accounts for first switch
    return changeL2[0].getValue();
    } else if (system.getTime() < 165.) { // Accounts for the second switch L2 path
    return changeL2[1].getValue();
    } else if (system.getTime() < 175.) { // Accounts for the second switch L2d path
    return changeL2[2].getValue();
    } else {
    return changeL2[3].getValue();
    }
    });
    
    WormSystem test_system = new WormSystem(new WormGroup(type.get("adult"), 1)) {
    @Override
    public void customInit() {
    
    }
    @Override
    public void customAction() {
    
    }
    };
    
    WormArray array = new WormArray(new File("data/t22"), new File("t22.txt"), test_system) {
    @Override
    protected void customInit(BufferedWriter out_io) throws IOException {
    out_io.write("\tTime\tPopulation\tDauers");
    }
    @Override
    protected void customOutput(String modified_parameter, BufferedWriter out_io) throws IOException {
    out_io.write("\t" + this.system.getTime() + "\t" + this.system.getNumberOf("") + "\t" + this.system.getNumberOf("dauer"));
    }
    };
    
    array.addVariables(changeL1); array.addVariables(changeL2);
    array.run();
    }
    
    /**
    * TODO: Make this have an interface, maybe a window would be an idea.
    * @param args
    *//*
    public static void test21() {
    CElegans type = new CElegans("N2");
    CElegans type2 = new CElegans("Defect", "N2");
    type2.getList().stream().forEach((w) -> {w.setEmitRate(0.0);});
    VariableDecimal dec1 = new VariableDecimal("L2/L2d Choice", 10.0, 10.0, 0.2),
    dec2 = new VariableDecimal("L3/dauer Choice", 10.0, 10.0, 0.2);
    
    // TODO Reference these better.
    ((SplitWormEvent) type.get("L1").getChangeProtocol()).setCalculation((WormSystem system) -> {
    double value = dec1.getValue() * (system.getPheromone(type).getValue() / system.getFood());
    return value;
    });
    ((SplitWormEvent) type.get("L2d").getChangeProtocol()).setCalculation((WormSystem system) -> {
    double value = dec2.getValue() * (system.getPheromone(type).getValue() / system.getFood());
    return value;
    });
    
    ((SplitWormEvent) type2.get("L1").getChangeProtocol()).setCalculation((WormSystem system) ->
    dec1.getValue() * (system.getPheromone(type).getValue() / system.getFood())
    );
    ((SplitWormEvent) type2.get("L2d").getChangeProtocol()).setCalculation((WormSystem system) ->
    dec2.getValue() * (system.getPheromone(type).getValue() / system.getFood())
    );
    
    // VariableDecimal weighted_dauer = new VariableDecimal("Weighted Dauer", 0.0, Double.POSITIVE_INFINITY, 1.0);
    WormSystem test_system = new WormSystem(new WormGroup(type.get("adult"), 1)) {
    @Override
    public void customInit() {
    prev_dauer = this.getNumberOf("dauer");
    // weighted_dauer.setValue(Double.valueOf(prev_dauer));
    }
    private int prev_dauer = 0;
    private final double mean_lifetime = 200.0;
    @Override
    public void customAction() {
    int next_dauer = this.getNumberOf("dauer");
    if(prev_dauer != next_dauer) {
    /*weighted_dauer.setValue(weighted_dauer.getValue() +
    (this.getNumberOf("dauer") - prev_dauer) *
    Math.exp(- this.getTime() / mean_lifetime));*//*
    prev_dauer = next_dauer;
    }
    }
    };
    // test_system.getInitialFood().set(1000000.0, 8000000.0, 50000.0);
    // test_system.enableOutput();
    
    WormArray array = new WormArray(new File("data/t2"), new File("t2.txt"), test_system) {
    @Override
    protected void customInit(BufferedWriter out_io) throws IOException {
    out_io.write("\tTime\tPopulation\tDauers" /*+ "\tWeighted"*//*);
    }
    @Override
    protected void customOutput(String modified_parameter, BufferedWriter out_io) throws IOException {
    out_io.write("\t" + this.system.getTime() + "\t" + this.system.getNumberOf("") + "\t" + this.system.getNumberOf("dauer")/* + "\t" + weighted_dauer.getValue()*//*);
    }
    };
    
    array.addVariables(dec1, dec2);
    
    array.run();
    }
    
    public static void test2() {
    CElegans type = new CElegans("N2");
    VariableDecimal dec1 = new VariableDecimal("Scale", 0.0, 0.2, 0.005),
    dec2 = new VariableDecimal("Power", 0.0, 0.2, 0.005);
    
    // TODO Reference these better.
    ((SplitWormEvent) type.get("L1").getChangeProtocol()).setCalculation((WormSystem system) ->
    1.0 - dec1.getValue() * Math.exp(- dec2.getValue() *
    system.getPheromone(type).getValue() / system.getFood())
    );
    ((SplitWormEvent) type.get("L2d").getChangeProtocol()).setCalculation((WormSystem system) ->
    1.0 - dec1.getValue() * Math.exp(- dec2.getValue() *
    system.getPheromone(type).getValue() / system.getFood())
    );
    
    WormSystem test_system = new WormSystem(new WormGroup(type.get("egg"), 20));
    // test_system.getInitialFood().set(100000.0, 800000.0, 5000.0);
    // test_system.disableOutput();
    
    WormArray array = new WormArray(new File("data/t1"), new File("t1.txt"), test_system);
    
    array.addVariables(dec1, dec2);
    
    array.run();
    }
    
    public static final DecimalFormat format = new DecimalFormat("0.000");
    public static void test() {
    CElegans type = new CElegans("N2");
    VariableDecimal dec1 = new VariableDecimal("Scale", 0.0, 0.2, 0.005),
    dec2 = new VariableDecimal("Power", 0.0, 0.2, 0.005);
    
    // TODO Reference these better.
    ((SplitWormEvent) type.get("L1").getChangeProtocol()).setCalculation((WormSystem system) ->
    1.0 - dec1.getValue() * Math.exp(- dec2.getValue() *
    system.getPheromone(type).getValue() / system.getFood())
    );
    ((SplitWormEvent) type.get("L2d").getChangeProtocol()).setCalculation((WormSystem system) ->
    1.0 - dec1.getValue() * Math.exp(- dec2.getValue() *
    system.getPheromone(type).getValue() / system.getFood())
    );
    
    WormSystem test_system = new WormSystem(new WormGroup(type.get("egg"), 1));
    // test_system.getInitialFood().set(100000.0, 800000.0, 5000.0);
    for(dec1.setToMin(); dec1.hasStepUp(); dec1.stepUp()) {
    for(dec2.setToMin(); dec2.hasStepUp(); dec2.stepUp()) {
    test_system.setOutputFile(new File("data/D1 " + format.format(dec1.getValue())
    + ".D2 " + format.format(dec2.getValue()) + ".txt"));
    test_system.run();
    }
    }
    }*/
//</editor-fold>
}