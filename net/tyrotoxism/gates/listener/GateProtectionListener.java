package net.tyrotoxism.gates.listener;

import java.util.Arrays;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;

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
    public void onGateBlockBreak(final BlockBreakEvent event) {
    
        final Block block = event.getBlock();
        
        if (!Arrays.asList(Gates.blocks).contains(block.getType())) { return; }
        
        final Gate gate = this.plugin.searchGate(block);
        
        if (gate == null) { return; }
        
        if (gate.getSolidBlocks().contains(block) || gate.getGateBlocks().contains(block)) {
            
            event.setCancelled(true);
            
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateBlockPlace(final BlockPlaceEvent event) {
    
        final Block block = event.getBlock();
        final Gate gate = this.plugin.searchGate(block);
        
        if (gate == null) { return; }
        
        if (gate.getSolidBlocks().contains(block) || gate.getGateBlocks().contains(block)) {
            
            event.setCancelled(true);
            
        }
        
    }
    
}
