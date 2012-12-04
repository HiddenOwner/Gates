package net.tyrotoxism.gates;

public class GateType {
    
    private final String name;
    private final int delay;
    private final GateRedstone redstone;
    private final int radius;
    private final int width;
    private final boolean branches;
    
    public GateType(final String name, final int delay, final GateRedstone redstone, final int radius, final int width, final boolean branches) {
    
        this.name = name;
        this.delay = delay;
        this.redstone = redstone;
        this.radius = radius;
        this.width = width;
        this.branches = branches;
        
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
    
    public int getSearchRadius() {
    
        return this.radius;
        
    }
    
    public int getMaximumWidth() {
    
        return this.width;
        
    }
    
    public boolean getBranches() {
    
        return this.branches;
        
    }
    
}
