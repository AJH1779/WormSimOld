/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public class MonteCarloWormArray implements Runnable {
    public MonteCarloWormArray(Supplier<MonteCarloWormThread> thread_gen, int count) {
        threads = new Thread[count];
        montecarlos = new MonteCarloWormThread[count];
        for(int i = 0; i < threads.length; i++) {
            montecarlos[i] = thread_gen.get();
            montecarlos[i].disableOutput();
        }
    }
    private final Thread[] threads;
    private final MonteCarloWormThread[] montecarlos;

    @Override
    public void run() {
        for(int i = 0; i < threads.length; i++) {
            threads[i] = montecarlos[i].start();
        }
        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MonteCarloWormArray.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File("output.txt")))) {
            montecarlos[0].openOutput(output);
            for(MonteCarloWormThread mc : montecarlos) {
                mc.output(output);
            }
        } catch (IOException ex) {
            Logger.getLogger(MonteCarloWormArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}