package net.tyrotoxism.gates.event;

import net.tyrotoxism.gates.Gate;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class GateEvent extends Event {
    
    private final Gate gate;
    private final Player player;
    
    public GateEvent(final Gate gate, final Player player) {
    
        this.gate = gate;
        this.player = player;
        
    }
    
    public Gate getGate() {
    
        return this.gate;
        
    }
    
    public Player getPlayer() {
    
        return this.player;
        
    }
    
}
