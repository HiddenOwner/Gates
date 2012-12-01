package net.tyrotoxism.gates;

public class GateType {
    
    private final String name;
    private final int delay;
    private final int interval;
    private final String permission;
    
    public GateType(final String name, final int delay, final int interval, final String permission) {
    
        this.name = name;
        this.delay = delay;
        this.interval = interval;
        this.permission = permission;
        
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
    
    public String getPermission() {
    
        return this.permission;
        
    }
    
}
