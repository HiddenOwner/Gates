package com.geekzmultimedia.gates.event;

import com.geekzmultimedia.gates.Gate;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class GateActivationEvent extends GateEvent implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final ActivationAction action;
    
    public GateActivationEvent(final Gate gate, final Player player, final ActivationAction action) {
    
        super(gate, player);
        this.cancelled = false;
        this.action = action;
        
    }
    
    public ActivationAction getAction() {
    
        return this.action;
        
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
    
    public enum ActivationAction {
        
        PLAYER, REDSTONE;
        
    }
    
}
