/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.values.RandomValue;
import com.ajh1779.worms.values.VariableValue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The second version needs to perform statistical collections as well as a
 * Monte Carlo simulation.
 * @author Arthur
 */
public abstract class WormThread implements Runnable {
    private static final Logger LOG = Logger.getLogger(WormThread.class.getName());
    /**
     * Creates a worm thread object with the specified system.
     * @param system
     */
    public WormThread(WormSystem system) {
        this.system = system;
    }
    protected final WormSystem system;
    
    //<editor-fold defaultstate="collapsed" desc="Output Control">
    private boolean do_output = false;
    private File sys_dir = new File("thread_dir"), thread_file = new File("thread_test.txt");
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
     * Enables the output for the system. By default this is disabled.
     * Beware that this is intensive if performed during a thread.
     */
    public void enableSystemOutput() {
        system.enableOutput();
    }
    /**
     * Disables the output for the system.
     */
    public void disableSystemOutput() {
        system.disableOutput();
    }
    /**
     * Returns true if the output for the system is enabled. By default is
     * disabled. Beware this is intensive if performed during a thread.
     * @return 
     */
    public boolean systemOutputEnabled() {
        return system.outputEnabled();
    }
    
    public void setOutputFiles(File thread_file, File system_directory) {
        this.thread_file = thread_file;
        this.sys_dir = system_directory;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Add, Remove, and Clear Variables">
    protected final ArrayList<VariableValue> variables = new ArrayList<>();
    protected final ArrayList<RandomValue> iterators = new ArrayList<>();
    /**
     * Adds variables for the thread to run over.
     * @param decs 
     */
    public void addVariables(VariableValue... decs) {
        variables.addAll(Arrays.asList(decs));
    }
    /**
     * Removes variables for the thread to run over.
     * @param decs 
     */
    public void removeVariables(VariableValue... decs) {
        variables.removeAll(Arrays.asList(decs));
    }
    /**
     * Clears all variables the thread will run over.
     */
    public void clearVariables() {
        variables.clear();
    }
    
    /**
     * Adds variables for the thread to iterate over on each loop.
     * @param decs 
     */
    public void addIterators(RandomValue... decs) {
        iterators.addAll(Arrays.asList(decs));
    }
    /**
     * Removes variables for the thread to iterate over on each loop.
     * @param decs 
     */
    public void removeIterators(RandomValue... decs) {
        iterators.removeAll(Arrays.asList(decs));
    }
    /**
     * Clears the variables the thread iterates over on each loop. 
     */
    public void clearIterators() {
        iterators.clear();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Get Current System Values">
    private int variable_counter = 0;
    private int iteration_counter = 0;
    /**
     * Returns the variable change iteration the program is currently on.
     * @return
     */
    public int getVariableIterationCount() {
        return variable_counter;
    }
    /**
     * Returns the current variable configuration iteration the program is
     * currently on.
     * @return
     */
    public int getCurrentIterationCount() {
        return iteration_counter;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Variable For-Loop">
    private String mod_out;
    private boolean hasStepComplete = true;
    /**
     * Resets all of the variables to their initial states, and sets the flag
     * for checking whether all the variables have reached the final step.
     */
    protected void resetVariables() {
        hasStepComplete = false;
        if(!variables.isEmpty()) {
            mod_out = variables.get(0).toString();
        }
        variables.stream().forEach((v) -> {
            v.setToMin();
        });
    }
    /**
     * Returns true if there is another step for the variables to go to in order
     * to cover all combinations of variables. That is, the second time that all
     * variables have hasStepUp() == true.
     * @return
     */
    protected boolean hasVariablesStep() {
        return variables.stream().anyMatch((v) -> (v.hasStepUp()))
                || (hasStepComplete = !hasStepComplete);
    }
    /**
     * Steps the next variable up, so as to iterate through all combinations of
     * variables. Note this is quite slow relative to random sampling towards
     * a desired result.
     */
    protected void stepVariables() {
        for(int i = 0; i < variables.size(); i++) {
            VariableValue v = variables.get(i);
            if(v.hasStepUp()) {
                v.stepUp();
                for(int j = 0; j < i; j++) {
                    variables.get(j).setToMin();
                }
                mod_out = v.toString();
                break;
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Iterator For-Loop">
    /**
     * Resets the iteration counter and randomises the iterator values.
     */
    protected void resetIterators() {
        stepIterators();
    }
    /**
     * Returns true if there is another iteration step to pass through. That is,
     * if the maximum number of iterations for this loop has not been reached.
     * @return
     */
    protected boolean hasIterationStep() {
        return iteration_counter < 1;
    }
    /**
     * Increases the iteration counter
     */
    protected void stepIterators() {
        iterators.stream().forEach((v) -> {
            v.random();
        });
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Core Run Program">
    /**
     * Called at the beginning of the thread before any simulations have taken
     * place and before the output has been created.
     * This is for setting any custom data to initial conditions.
     */
    protected abstract void preOutputInit();
    /**
     * Called at the beginning of the thread before any simulations have taken
     * place and after the output has been created.
     * This is for setting any custom data to initial conditions.
     */
    protected abstract void postOutputInit();
    /**
     * Called on each loop of the thread, before the iterations of the
     * system have been fulfilled.
     */
    protected abstract void preLoopAction();
    /**
     * Called on each loop of the thread, after the iterations of the
     * system have been fulfilled and before the output has been called.
     */
    protected abstract void postLoopPreOutputAction();
    /**
     * Called on each loop of the thread, after the iterations of the
     * system have been fulfilled and after the output has been called.
     */
    protected abstract void postLoopPostOutputAction();
    /**
     * Called on each run of the system. This is used to handle data collected
     * during each iteration run to be combined in preparation for the
     * <code>output()</code> method.
     */
    protected abstract void iterAction();
    
    @Override
    public void run() {
        lock.lock();
        try {
            // Open the program output.
            preOutputInit();
            if(do_output) openOutput();
            postOutputInit();
            // Initialise the program parameters
            variable_counter = 0;
            // The for loop for the variables of the system.
            for(resetVariables(); hasVariablesStep(); stepVariables()) {
                // The for loop for the iterators of the system. The results are
                // tallied with the iteration() method.
                iteration_counter = 0;
                preLoopAction();
                for(resetIterators(); hasIterationStep(); stepIterators()) {
                    // Note that enabling output for the system is significantly
                    // recommended against.
                    if(do_output && system.outputEnabled())
                        system.setOutputFile(new File(sys_dir, "F" +
                                variable_counter + "." + iteration_counter + ".txt"));
                    system.run();
                    // Tallies up the relevant system results.
                    iterAction();
                    iteration_counter++;
                }
                // Outputs the line for this variable set.
                postLoopPreOutputAction();
                if(do_output) output(output);
                postLoopPostOutputAction();
                variable_counter++;
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE,
                    "IO Failure", ex);
        } finally {
            // Closes the Output stream.
            closeOutput();
            condition.signal();
            lock.unlock();
        }
    }
    
    public void runSystem() {
        system.run();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Output Methods">
    private BufferedWriter output;
    
    /**
     * Outputs the header line for the file, aligning the data type names to the
     * data lines below. Use customOpenOutput() to add more names onto the header
     * line.
     * @throws IOException
     */
    private void openOutput() throws IOException {
        Logger.getLogger(WormArray.class.getSimpleName()).log(Level.INFO,
                "Opening File: {0}", thread_file.getAbsolutePath());
        File parent = thread_file.getParentFile();
        if(parent != null) parent.mkdirs();
        output = new BufferedWriter(new FileWriter(thread_file));
        openOutput(output);
    }
    /**
     * Outputs the header line for the file, aligning the data type names to the
     * data lines below. Use customOpenOutput() to add more names onto the header
     * line.
     * @throws IOException
     */
    protected final void openOutput(BufferedWriter output) throws IOException {
        boolean first = false;
        for(VariableValue v : variables) {
            output.write((first ? "\t" : "") + v.getName());
            first = true;
        }
        customOpenOutput(output);
        output.newLine();
    }
    /**
     * Outputs a data line for the current configuration of variables.
     * Use customOutput() to add more values onto the data line.
     * @param output
     * @throws IOException
     */
    protected final void output(BufferedWriter output) throws IOException {
        boolean first = false;
        for(VariableValue v : variables) {
            output.write((first ? "\t" : "") + v.getValue());
            first = true;
        }
        customOutput(output);
        output.newLine();
    }
    /**
     * Closes the file output stream.
     */
    protected final void closeOutput() {
        if(output != null) {
            try {
                output.close();
                LOG.log(Level.INFO,
                        "Closed File: {0}", thread_file.getAbsolutePath());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE,
                        "Could not close output correctly.", ex);
            }
        }
    }
    /**
     * Appends data to the end of the open output line. That is, the line which
     * includes the data headers.
     * @param out_io The BufferedWriter currently outputting to the output file
     * @throws IOException
     */
    public abstract void customOpenOutput(BufferedWriter out_io) throws IOException;
    /**
     * Appends data to the end of the output line. That is, the current data output
     * line. This should line up with the headers outputted in <code>customOpenOutput()</code>.
     * @param out_io The BufferedWriter currently outputting to the output file.
     * @throws IOException
     */
    public abstract void customOutput(BufferedWriter out_io) throws IOException;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Synchro">
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    
    /**
     * Starts a thread for this simulation.
     * @return 
     */
    public Thread start() {
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }
    
    /**
     * Returns the lock for this thread.
     * @return 
     */
    public ReentrantLock getLock() {
        return lock;
    }
    
    /**
     * Returns the lock condition for this thread. The condition is released
     * when
     * @return 
     */
    public Condition getLockCondition() {
        return condition;
    }
    //</editor-fold>
}