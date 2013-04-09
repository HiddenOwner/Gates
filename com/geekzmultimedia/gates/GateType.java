package com.geekzmultimedia.gates;

public class GateType {
    
    private final String name;
    private final int delay;
    private final GateRedstone redstone;
    
    public GateType(final String name, final int delay, final GateRedstone redstone) {
    
        this.name = name;
        this.delay = delay;
        this.redstone = redstone;
        
    }
    
    public String getName() {
    
        return this.name;
        
    }
    
    public int getDelay() {
    
        return this.delay;
        
    }
    
    public GateRedstone getRedstone() {
    
        return this.redstone;
        
    }
    
}
