/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.values.VariableValue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public class WormArray implements Runnable {
    public WormArray(File dir, File reg, WormSystem system) {
        this.sys_dir = dir;
        this.arr_file = reg;
        this.system = system;
    }
    protected final ArrayList<VariableValue> variables = new ArrayList<>();
    private final File sys_dir, arr_file;
    protected final WormSystem system;

    public void addVariables(VariableValue... decs) {
        variables.addAll(Arrays.asList(decs));
    }
    public void clearVariables() {
        variables.clear();
    }
    
    @Override
    public void run() {
        try {
            openIO();
            long i = 0;
            for(reset(); hasStep(); step()) {
                i++;
                system.setOutputFile(new File(sys_dir, "F" + i + ".txt"));
                system.run();
                output(i);
            }
        } catch (IOException ex) {
            Logger.getLogger(WormArray.class.getName()).log(Level.SEVERE, 
                    "Failed to perform IO.", ex);
        } finally {
            closeIO();
        }
    }
    
    private String mod_out;
    private boolean hasStepComplete = true;
    public void reset() {
        hasStepComplete = false;
        if(!variables.isEmpty()) {
            mod_out = variables.get(0).getName();
        }
        variables.stream().forEach((v) -> {
            v.setToMin();
        });
    }
    public boolean hasStep() {
        return variables.stream().anyMatch((v) -> (v.hasStepUp())) || (hasStepComplete = !hasStepComplete);
    }
    public void step() {
        for(int i = 0; i < variables.size(); i++) {
            VariableValue v = variables.get(i);
            if(v.hasStepUp()) {
                v.stepUp();
                for(int j = 0; j < i; j++) {
                    variables.get(j).setToMin();
                }
                mod_out = v.getName();
                break;
            }
        }
    }
    
    private BufferedWriter out_io;
    private void openIO() throws IOException {
        Logger.getLogger(WormArray.class.getSimpleName()).log(Level.INFO,
                "Opening File: {0}", arr_file.getAbsolutePath());
        File parent = arr_file.getParentFile();
        if(parent != null) parent.mkdirs();
        out_io = new BufferedWriter(new FileWriter(arr_file));
        
        boolean first = false;
        if(system.outputEnabled()) {
            out_io.write("Filename"); first = true;
        }
        for(VariableValue v : variables) {
            out_io.write((first ? "\t" : "") + v.getName());
            first = true;
        }
        customInit(out_io);
        out_io.newLine();
    }
    private void output(long i) throws IOException {
        boolean first = false;
        if(system.outputEnabled()) {
            out_io.write("F" + i + ".txt");
        }
        for(VariableValue v : variables) {
            out_io.write((first ? "\t" : "") + v.getValue());
            first = true;
        }
        customOutput(mod_out, out_io);
        out_io.newLine();
    }
    protected void customInit(BufferedWriter out_io) throws IOException {
        out_io.write("\tDauers");
    }
    protected void customOutput(String modified_parameter, BufferedWriter out_io) throws IOException {
        out_io.write("\t" + system.getNumberOf("dauer"));
    }
    private void closeIO() {
        if(out_io != null) {
            try {
                out_io.close();
            } catch (IOException ex) {
                Logger.getLogger(WormArray.class.getName()).log(Level.SEVERE, 
                        "Could not close output correctly.", ex);
            }
        }
        Logger.getLogger(WormArray.class.getName()).log(Level.INFO,
                "Closed File: {0}", arr_file.getAbsolutePath());
    }
}