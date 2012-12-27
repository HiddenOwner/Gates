package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gates;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GateProtectionListener implements Listener {
    
    private final Gates plugin;
    
    public GateProtectionListener(final Gates plugin) {
    
        this.plugin = plugin;
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateBlockInteract(final PlayerInteractEvent event) {
    
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            
            event.setCancelled((this.plugin.searchGate(event.getClickedBlock()) != null) || (this.plugin.searchGate(event.getClickedBlock().getRelative(event.getBlockFace())) != null));
            
        } else {
            
            event.setCancelled((event.getAction() == Action.LEFT_CLICK_BLOCK) && (this.plugin.searchGate(event.getClickedBlock()) != null));
            
        }
        
    }
}
