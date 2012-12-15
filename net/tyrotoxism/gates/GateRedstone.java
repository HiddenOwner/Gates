package net.tyrotoxism.gates;

public enum GateRedstone {
    
    OFF("REDSTONE_OFF"), ON("REDSTONE_ON"), TOGGLE("REDSTONE_TOGGLE");
    
    private String name;
    
    GateRedstone(final String name) {
    
        this.name = name;
        
    }
    
    public String getName() {
    
        return this.name;
        
    }
    
    public static GateRedstone getByName(final String name) {
    
        for (final GateRedstone redstone : GateRedstone.values()) {
            
            if (redstone.getName().equals(name)) { return redstone; }
            
        }
        
        return null;
        
    }
    
}
