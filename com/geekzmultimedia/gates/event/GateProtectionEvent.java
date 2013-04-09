package com.geekzmultimedia.gates.event;

import com.geekzmultimedia.gates.Gate;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class GateProtectionEvent extends GateEvent implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final boolean place;
    private final Block block;
    
    public GateProtectionEvent(final Gate gate, final Player player, final boolean place, final Block block) {
    
        super(gate, player);
        this.cancelled = true;
        this.place = place;
        this.block = block;
        
    }
    
    public boolean isBlockPlace() {
    
        return this.place;
        
    }
    
    public Block getBlock() {
    
        return this.block;
        
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
    
        return GateProtectionEvent.handlers;
        
    }
    
    public static HandlerList getHandlerList() {
    
        return GateProtectionEvent.handlers;
    }
    
}
