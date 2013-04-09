package com.geekzmultimedia.gates.event;

import com.geekzmultimedia.gates.Gate;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class GateDestructionEvent extends GateEvent implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    
    public GateDestructionEvent(final Gate gate, final Player player) {
    
        super(gate, player);
        this.cancelled = false;
        
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
    
        return GateDestructionEvent.handlers;
        
    }
    
    public static HandlerList getHandlerList() {
    
        return GateDestructionEvent.handlers;
    }
    
}
