package net.tyrotoxism.gates.event;

import net.tyrotoxism.gates.Gate;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GateActivationEvent extends Event implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Gate gate;
    private final Player player;
    private boolean cancelled;
    
    public GateActivationEvent(final Gate gate, final Player player) {
    
        this.gate = gate;
        this.player = player;
        this.cancelled = false;
        
    }
    
    public Gate getGate() {
    
        return this.gate;
        
    }
    
    public Player getPlayer() {
    
        return this.player;
        
    }
    
    @Override
    public boolean isCancelled() {
    
        return this.cancelled;
        
    }
    
    @Override
    public void setCancelled(final boolean cancelled) {
    
        this.cancelled = cancelled;
        
    }
    
    @Override
    public HandlerList getHandlers() {
    
        return GateActivationEvent.handlers;
        
    }
    
    public static HandlerList getHandlerList() {
    
        return GateActivationEvent.handlers;
    }
    
}
