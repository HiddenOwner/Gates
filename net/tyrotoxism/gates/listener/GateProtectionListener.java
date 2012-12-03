package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateProtectionEvent;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class GateProtectionListener implements Listener {
    
    private final Gates plugin;
    
    public GateProtectionListener(final Gates plugin) {
    
        this.plugin = plugin;
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(final BlockBreakEvent event) {
    
        // Search for gate, gate sign is output in variable "block"
        
        final Block block = event.getBlock();
        final Gate gate = this.plugin.getGate(block);
        
        if (gate == null) { return; }
        
        final GateProtectionEvent evt = new GateProtectionEvent(gate, event.getPlayer(), false, event.getBlock());
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        event.setCancelled(evt.isCancelled());
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(final BlockPlaceEvent event) {
    
        // Search for gate, gate sign is output in variable "block"
        
        final Block block = event.getBlock();
        final Gate gate = this.plugin.getGate(block);
        
        if (gate == null) { return; }
        
        final GateProtectionEvent evt = new GateProtectionEvent(gate, event.getPlayer(), true, event.getBlock());
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        event.setCancelled(evt.isCancelled());
        
    }
    
}
