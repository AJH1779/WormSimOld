/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.calculations.TanhWormCalculation;
import com.ajh1779.worms.events.DirectWormEvent;
import com.ajh1779.worms.events.ReproduceWormEvent;
import com.ajh1779.worms.events.SplitWormEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An object representing a species of worm. Each thread that modifies the
 * properties of the worm species should have independent worm species.
 * @author Arthur
 */
public abstract class WormSpecies {
    /**
     * Creates a new worm species with the specified name and a corresponding
     * pheromone it produces named "[name] Pheromone", where [name] is the provided
     * name.
     * @param name The name of the species.
     */
    public WormSpecies(String name) {
        this(name, name + " Pheromone");
    }
    /**
     * Creates a new worm species with the specified name and a corresponding
     * pheromone it produces with the second specified name.
     * @param name The name of the species.
     * @param pheroname The name of the pheromone it produces.
     */
    public WormSpecies(String name, String pheroname) {
        this.name = name;
        this.pheromone = pheroname;
    }
    
    /**
     * Creates the initial settings for the worm species. Must be called before
     * running the system for the first time, unless it was created by
     * <code>copy()</code>.
     */
    public abstract void create();
    /**
     * Creates a copy of this worm species.
     * @return 
     */
    public abstract WormSpecies copy();
    
    //<editor-fold defaultstate="collapsed" desc="Names">
    private final String name;
    private final String pheromone;
    /**
     * Returns the name of the worm species.
     * @return The name of this species.
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the name of the pheromone.
     * @return The name of this species pheromone.
     */
    public String getPheromoneName() {
        return pheromone;
    }
    //</editor-fold>
    
    /**
     * Returns the worm type with the specified name.
     * @param str
     * @return 
     */
    public abstract WormType get(String str);
    /**
     * Returns a list of the WormTypes this species uses.
     * @return 
     */
    public abstract List<WormType> getList();
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WormSpecies other = (WormSpecies) obj;
        return Objects.equals(this.pheromone, other.pheromone) &&
                Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.pheromone);
        return hash;
    }
    
    /**
     * An object representing the species of worm known as C.Elegans.
     */
    public static class CElegans extends WormSpecies {
        /**
         * Creates a new worm species of the CElegans variety with the specified
         * name and a corresponding pheromone it produces named "[name] Pheromone",
         * where [name] is the provided name.
         * @param name The species name.
         */
        public CElegans(String name) {
            this(name, name + " Pheromone");
        }
        /**
         * Creates a new worm species of the CElegans variety with the specified
         * name and a corresponding pheromone it produces with the second specified
         * name.
         * @param name The name of the species.
         * @param pheroname The name of the pheromone it produces.
         */
        public CElegans(String name, String pheroname) {
            super(name, pheroname);
            list = Collections.unmodifiableList(Arrays.asList(new WormType[]{
                egg = new WormType(this, name + "egg", 18.0, 0.0, 0.0),
                L1 = new WormType(this, name + "L1", 15.0, 1.10, 1.0),
                L2 = new WormType(this, name + "L2", 9.0, 2.22, 1.0),
                L3 = new WormType(this, name + "L3", 9.0, 3.73, 1.0),
                L4 = new WormType(this, name + "L4", 12.0, 5.50, 1.0),
                L2d = new WormType(this, name + "L2d", 16.0, 2.22, 1.0),
                dauer = new WormType(this, name + "dauer", Double.POSITIVE_INFINITY, 0.0, 0.0),
                adult1 = new WormType(this, name + "D1adult", 24.0, 12.2, 1.0),
                adult2 = new WormType(this, name + "D2adult", 24.0, 12.2, 1.0),
                adult3 = new WormType(this, name + "D3adult", 24.0, 12.2, 1.0),
                adult4 = new WormType(this, name + "D4adult", 24.0, 12.2, 1.0)
            }));
            
            egg.setChangeProtocol(new DirectWormEvent(L1));
            L1.setChangeProtocol(new SplitWormEvent(L2, L2d));
            L2.setChangeProtocol(new DirectWormEvent(L3));
            L3.setChangeProtocol(new DirectWormEvent(L4));
            L4.setChangeProtocol(new DirectWormEvent(adult1));
            L2d.setChangeProtocol(new SplitWormEvent(L3, dauer));
            adult1.setChangeProtocol(new ReproduceWormEvent(adult2, egg, 65.0));
            adult2.setChangeProtocol(new ReproduceWormEvent(adult3, egg, 125.0));
            adult3.setChangeProtocol(new ReproduceWormEvent(adult4, egg, 80.0));
            adult4.setChangeProtocol(new DirectWormEvent(egg, 20.0));
        }
        private final List<WormType> list;
        private final WormType egg, L1, L2, L3, L4, L2d, dauer, adult1, adult2, adult3, adult4;

        /**
         * This change protocol sets the two SplitWormEvent change protocols for
         * the L1 and L2d worms.
         */
        @Override
        public void create() {
            ((SplitWormEvent)  L1.getChangeProtocol()).setCalculation(new TanhWormCalculation("L1", this));
            ((SplitWormEvent) L2d.getChangeProtocol()).setCalculation(new TanhWormCalculation("L2d", this));
        }
        
        @Override
        public CElegans copy() {
            CElegans species = new CElegans(getName(), getPheromoneName());
            for(int i = 0; i < list.size(); i++) {
                species.list.get(i).set(list.get(i));
            }
            return species;
        }
        
        /**
         * Returns values for "egg", "L1", "L2", "L3", "L4", "L2d", "dauer",
         * "D1adult", "D2adult", "D3adult", "D4adult".
         * @param str
         * @return 
         */
        @Override
        public WormType get(String str) {
            switch(str) {
                case "egg": return egg;
                case "L1": return L1;
                case "L2": return L2;
                case "L3": return L3;
                case "L4": return L4;
                case "L2d": return L2d;
                case "dauer": return dauer;
                case "D1adult": return adult1;
                case "D2adult": return adult2;
                case "D3adult": return adult3;
                case "D4adult": return adult4;
                default:
                    throw new IllegalArgumentException(
                        "Supports only: \"egg\", \"L1\", \"L2\", \"L3\", \"L4\", "
                                + "\"L2d\", \"dauer\", \"D1adult\", \"D2adult\", "
                                + "\"D3adult\", \"D4adult\"");
            }
        }
        @Override
        public List<WormType> getList() {
            return list;
        }
    }
}