package net.tyrotoxism.gates;

public class GateType {
    
    private final String name;
    private final int delay;
    private final int interval;
    private final GateRedstone redstone;
    
    public GateType(final String name, final int delay, final int interval, final GateRedstone redstone) {
    
        this.name = name;
        this.delay = delay;
        this.interval = interval;
        this.redstone = redstone;
        
    }
    
    public String getName() {
    
        return this.name;
        
    }
    
    public int getDelay() {
    
        return this.delay;
        
    }
    
    public int getInterval() {
    
        return this.interval;
        
    }
    
    public GateRedstone getRedstone() {
    
        return this.redstone;
        
    }
    
}
