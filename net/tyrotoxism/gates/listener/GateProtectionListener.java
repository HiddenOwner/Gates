package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gates;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class GateProtectionListener implements Listener {
    
    private final Gates plugin;
    
    public GateProtectionListener(final Gates plugin) {
    
        this.plugin = plugin;
        this.plugin.equals(null); // Pointless stuff
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateBlockBreak(final BlockBreakEvent event) {
    
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateBlockPlace(final BlockPlaceEvent event) {
    
    }
    
}
