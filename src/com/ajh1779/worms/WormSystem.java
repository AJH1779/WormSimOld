/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.events.WormEvent;
import com.ajh1779.worms.values.RandomValue;
import com.ajh1779.worms.values.VariableDecimal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The system of worms that simulation runs. Implements the runnable interface
 * to allow 
 * @author Arthur
 */
public class WormSystem implements Runnable {
    /**
     * Creates a worm system with a <code>VariableDecimal</code> object
     * representing the initial food amount.
     */
    public WormSystem() {
        this(new VariableDecimal("Initial Food", 400000.0));
    }
    /**
     * Creates a worm system with the provided initial food amount.
     * @param init_food 
     */
    public WormSystem(RandomValue<Double> init_food) {
        this.init_food = init_food;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Output Control">
    private boolean do_output = false;
    private File io_file = new File("system_output.txt");
    
    /**
     * Enables the file output for the system. This records the exact procession
     * of the simulation.
     */
    public void enableOutput() {
        do_output = true;
    }
    /**
     * Disables the file output for the system. This records the exact procession
     * of the simulation.
     */
    public void disableOutput() {
        do_output = false;
    }
    /**
     * Returns true if the output is enabled. By default it is disabled.
     * @return
     */
    public boolean outputEnabled() {
        return do_output;
    }
    
    /**
     * Sets the file to write to.
     * @param file 
     */
    public void setOutputFile(File file) {
        this.io_file = file;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Worm Event Adding, Removing, and Clearing">
    // Stores the defined events
    protected final HashSet<WormEvent> initial_parameters = new HashSet<>();
    protected final HashSet<WormSpecies> species = new HashSet<>();
    protected final HashSet<WormGroup> worm_groups = new HashSet<>();
    /**
     * Adds events and groups to the initial parameters of the system.
     * @param groups_and_events
     */
    public void addInitialWormEventsAndGroups(WormEvent... groups_and_events) {
        // The initial parameters of the system are set based on the input.
        initial_parameters.addAll(Arrays.asList(groups_and_events));
        // For each event within the provided arguments
        for(WormEvent e : groups_and_events) {
            // If it is a wormgroup and the list of species does not contain the species in the group
            if(e instanceof WormGroup &&
                    !species.contains(((WormGroup) e).getType().getSpecies())) {
                WormGroup g = (WormGroup) e;
                // add the group to the species list
                species.add(g.getType().getSpecies());
                // add the pheromone to the list of pheromones if it is absent
                // TODO: Have unlimited number of pheromones
                pheromones.putIfAbsent(g.getType().getSpecies().getPheromoneName(),
                        new WormPheromone(g.getType().getSpecies().getPheromoneName()));
            }
        }
    }
    /**
     * Removes events and groups from the initial parameters of the system.
     * @param groups_and_events
     */
    public void removeInitialWormEventOrGroup(WormEvent... groups_and_events) {
        initial_parameters.removeAll(Arrays.asList(groups_and_events));
        
        for(WormEvent e : groups_and_events) {
            if(e instanceof WormGroup) {
                // Check if need to remove the species
                boolean flag_species = false, flag_pheromone = false;
                for(WormEvent e2 : initial_parameters) {
                    if(e2 instanceof WormGroup) {
                        if(((WormGroup) e2).getType().getSpecies() == ((WormGroup) e).getType().getSpecies()) {
                            flag_species = true; flag_pheromone = true;
                            break;
                        } else {
                            flag_pheromone = flag_pheromone || ((WormGroup) e2).getType().getSpecies().getPheromoneName().equals(((WormGroup) e).getType().getSpecies().getPheromoneName());
                        }
                    }
                }
                if(!flag_species) {
                    species.remove(((WormGroup) e).getType().getSpecies());
                }
                if (!flag_pheromone) {
                    pheromones.remove(((WormGroup) e).getType().getSpecies().getPheromoneName());
                }
            }
        }
    }
    /**
     * Clears the initial parameters of the system.
     */
    public void clearInitialWormEventsAndGroups() {
        initial_parameters.clear();
        species.clear();
        pheromones.clear();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="System Variable Access">
    private final RandomValue<Double> init_food;
    private final VariableDecimal init_time = new VariableDecimal("Initial Time", 0.0);
    private final VariableDecimal max_time = new VariableDecimal("Max Time", Double.POSITIVE_INFINITY);
    protected final HashMap<String, WormPheromone> pheromones = new HashMap<>();
    /**
     * Returns the initial food of the system.
     * @return
     */
    public RandomValue<Double> getInitialFood() {
        return init_food;
    }
    /**
     * Returns the initial time of the system.
     * @return
     */
    public VariableDecimal getInitialTime() {
        return init_time;
    }
    /**
     * Returns the maximum time of the system.
     * @return
     */
    public VariableDecimal getMaxTime() {
        return max_time;
    }
    public WormPheromone getPheromone(String pheroname) {
        return pheromones.get(pheroname);
    }
    /**
     * Returns the current pheromone in the system that is produced by the specified
     * species.
     * @param species The worm species pheromone
     * @return The amount of pheromone
     */
    public WormPheromone getPheromone(WormSpecies species) {
        return pheromones.get(species.getPheromoneName());
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Get Current System Values">
    private double food = 10000.0;
    private double time = 0.0;
    /**
     * Returns the current food in the system.
     * @return The system food quantity.
     */
    public double getFood() {
        return food;
    }
    /**
     * Returns the current time of the system.
     * @return The system time
     */
    public double getTime() {
        return time;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Modify Current System Values">
    /**
     * Adds a group to the system currently being run. Does not alter the initial
     * conditions.
     * @param type
     * @param count
     **/
    public void addGroup(WormType type, int count) {
        if(count > 0) {
            WormGroup group = new WormGroup(type, count, time);
            worm_groups.add(group);
            event_queue.add(group);
        }
    }
    /**
     * Consumes the specified amount of food from the system.
     * @param f 
     */
    public void consumeFood(double f) {
        food -= f;
    }
    /**
     * Emits the specified amount of pheromone.
     * @param species The Worm Species
     * @param delt The time step
     * @param rate The Pheromone Rate
     */
    public void emitPheromone(WormSpecies species, double delt, double rate) {
        pheromones.get(species.getPheromoneName()).modify(delt, rate);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Core Run Program">
    private final PriorityQueue<WormEvent> event_queue = new PriorityQueue<>();
    /**
     * Resets the system to the initial conditions. Called at the beginning of
     * each run.
     */
    public void reset() {
        food = init_food.getValue();
        pheromones.entrySet().stream().forEach((e) -> {e.getValue().reset();});
        time = init_time.getValue();
        event_queue.clear();
        worm_groups.clear();
        initial_parameters.stream().forEach((evt) -> {
            WormEvent evtcpy = evt.copy();
            event_queue.add(evtcpy);
            if(evt instanceof WormGroup) {
                worm_groups.add(((WormGroup) evtcpy));
            }
        });
    }
    /**
     * Advances the system time by the specified amount. This is called between
     * events.
     * @param new_time 
     */
    public void progress(double new_time) {
        double delt = new_time - time;
        time = new_time;
        
        worm_groups.stream().forEach((group) -> {
            group.progress(this, delt);
        });
        pheromones.entrySet().stream().forEach((e) -> {
            e.getValue().progress(delt);
        });
    }
    /**
     * A custom process that occurs at the start of the run loop.
     */
    public void customInit() {}
    /**
     * A custom process that occurs after each update in the loop.
     */
    public void customAction() {}
    /**
     * Runs the simulation. Implemented this way to allow for separately running
     * threads.
     */
    @Override
    public void run() {
        reset();
        
        // Some form of initial state output:
        try {
            customInit();
            if(do_output) {
                openOutput();
                customOpenOutput(output);
                output();
                customOutput(output);
            }
            
            // The simulation can run so long as there is food to eat and events to
            // process.
            while(food > 0.0f && !event_queue.isEmpty()) {
                // Gets the next event to be executed
                WormEvent evt = event_queue.poll();
                
                // Progress the simulation to the point when the next event occurs.
                if(evt.getTime() == Double.POSITIVE_INFINITY) {
                    break;
                } else if (evt.getTime() >= max_time.getValue()) {
                    progress(max_time.getValue());
                    break;
                } else {
                    progress(evt.getTime());
                }
                
                // Check that there was enough resources to get here
                if(food < 0.0f) {
                    break;
                }
                // Run the event, using this system as the context
                evt.run(this);
                
                // Some form of output
                if(time < Double.POSITIVE_INFINITY) {
                    customAction();
                    if(do_output) {
                        output();
                    }
                }
                
                if(evt.willReoccur()) {
                    event_queue.add(evt);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(WormSystem.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(do_output) closeOutput();
            } catch (IOException ex) {
                Logger.getLogger(WormSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Output Methods">
    private BufferedWriter output;
    /**
     * Opens the IO Channel to the file if one is selected, or prints to the
     * normal output channel.
     * @throws java.io.IOException
     */
    public void openOutput() throws IOException {
        Logger.getLogger(WormSystem.class.getSimpleName()).log(Level.INFO,
                "Open File: {0}", io_file.getAbsolutePath());
        File parent = io_file.getParentFile();
        if(parent != null) {
            parent.mkdirs();
        }
        output = new BufferedWriter(new FileWriter(io_file)); // */
        
        // TODO: Output all initial data.
        output.write("Time\tFood");
        for(WormSpecies s : species) {
            for(WormType t : s.getList()) {
                output.write("\t" + t.getTypeName());
            }
        }
        for(Entry<String, WormPheromone> p : pheromones.entrySet()) {
            output.write("\t" + p.getKey());
        }
        output.newLine();
        // System.out.println("Time\tFood\tPhero\tPop\tegg\tL1\tL2\tL2d\tL3\tL4\tadult\tdauer");
    }
    /**
     * The place to put the output of the program to. Currently this is to the
     * program output but it needs to be moved to a file.
     * @throws java.io.IOException
     */
    public void output() throws IOException {
        output.write(time + "\t" + food);
        for(WormSpecies s : species) {
            for(WormType t : s.getList()) {
                output.write("\t" + getNumberOf(t.getTypeName()));
            }
        }
        for(Entry<String, WormPheromone> p : pheromones.entrySet()) {
            output.write("\t" + p.getValue().toString());
        }
        customOutput(output);
        output.newLine();
    }
    /**
     * Closes the IO Channel if one has been opened.
     * @throws java.io.IOException
     */
    public void closeOutput() throws IOException {
        output.close();
    }
    
    /**
     * Writes the string array to the output separated by tabs.
     * @param strs
     * @throws IOException 
     */
    public final void writeToOutput(String... strs) throws IOException {
        for (String str : strs) {
            output.write("\t" + str);
        }
    }
    
    /**
     * The first line added to the names of the outputs.
     * @param out_io The BufferedWriter to the File
     * @throws java.io.IOException
     */
    public void customOpenOutput(BufferedWriter out_io) throws IOException {}
    /**
     * The additional output values, separated by '\\t' tabs.
     * @param out_io 
     * @throws java.io.IOException 
     */
    public void customOutput(BufferedWriter out_io) throws IOException {}
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Worm Number Collection">
    /**
     * Returns the number of worms that end in the name string given.
     * Hence, "dauer" will return all worms that are dauers, whilst "N2dauer"
     * will only return dauers which are of the N2 species.
     * @param name
     * @return
     */
    public int getNumberOf(String name) {
        int count = 0;
        count = worm_groups.stream().filter((group) ->
                (group.getType().getTypeName().endsWith(name))).map((group) ->
                        group.getNumber())
                .reduce(count, Integer::sum);
        return count;
    }
    /**
     * Returns the number of worms of the specified type.
     * @param type
     * @return
     */
    public int getNumberOf(WormType type) {
        int count = 0;
        count = worm_groups.stream().filter((group) ->
                (group.getType() == type)).map((group) ->
                        group.getNumber())
                .reduce(count, Integer::sum);
        return count;
    }
    //</editor-fold>
}