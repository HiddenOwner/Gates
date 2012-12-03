package net.tyrotoxism.gates.event;

import net.tyrotoxism.gates.Gate;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class GateCreationEvent extends GateEvent implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    
    public GateCreationEvent(final Gate gate, final Player player) {
    
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
    
        return GateCreationEvent.handlers;
        
    }
    
    public static HandlerList getHandlerList() {
    
        return GateCreationEvent.handlers;
    }
    
}
