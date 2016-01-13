/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.worms;

import com.ajh1779.worms.events.WormChange;
import com.ajh1779.worms.events.WormEvent;

/**
 * A group of worms within the system.
 * @author Arthur
 */
public class WormGroup extends WormEvent {
    /**
     * Creates a group of worms of the specified type and count for the start
     * of the system.
     * @param type The group type.
     * @param count The group count.
     */
    public WormGroup(WormType type, int count) {
        this(type, count, 0.0);
    }
    /**
     * Creates a group of worms of the specified type and count for the specified
     * time of the system.
     * @param type The group type.
     * @param count The group count.
     * @param time The group time.
     */
    public WormGroup(WormType type, int count, double time) {
        super(time + type.getCycleLifetime().getValue());
        this.type = type;
        this.count = count;
    }
    /**
     * Creates a worm group which is a copy of the provided worm group.
     * @param group 
     */
    public WormGroup(WormGroup group) {
        super(group.getTime());
        this.type = group.type;
        this.count = group.count;
    }
    private WormType type;
    private int count;
    
    /**
     * Sets the type of worm of the group.
     * @param type 
     */
    public void setType(WormType type) {
        this.type = type;
        setTime(getTime() + type.getCycleLifetime().getValue());
    }
    /**
     * Sets the number of worms in the group.
     * @param no 
     */
    public void setNumber(int no) {
        count = no;
    }
    /**
     * Returns the type of worm of the group.
     * @return 
     */
    public WormType getType() {
        return type;
    }
    /**
     * Returns the number of worms in the group.
     * @return 
     */
    public int getNumber() {
        return count;
    }
    
    @Override
    public void run(WormSystem system) {
        WormChange change = type.getChangeProtocol();
        if(change != null) {
            change.run(this, system);
        }
    }
    
    /**
     * Called during the loop to modify the system across a period of time.
     * @param sys The system to modify
     * @param delt The length of time to simulate
     */
    public void progress(WormSystem sys, double delt) {
        type.progress(sys, delt, count);
    }
    
    @Override
    public boolean willReoccur() {
        return count > 0;
    }
    
    @Override
    public WormGroup copy() {
        return new WormGroup(this.type, this.count, this.getTime() - this.type.getCycleLifetime().getValue());
    }
    
    @Override
    public String toString() {
        return this.type.toString() + ", " + this.count;
    }
}