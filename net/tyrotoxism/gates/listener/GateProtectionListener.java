package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;

import org.bukkit.block.BlockFace;
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
    
        if (event.isCancelled()) { return; }
        
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            
            if (this.plugin.getGate(event.getClickedBlock()) != null) { return; }
            
            final Gate gate = this.plugin.searchGate(event.getClickedBlock());
            
            if (gate == null) { return; }
            
            event.setCancelled(gate.getGateBlocks().contains(event.getClickedBlock()) || gate.getSolidBlocks().contains(event.getClickedBlock()) || gate.getGateBlocks().contains(event.getClickedBlock().getRelative(BlockFace.UP)) || gate.getSolidBlocks().contains(event.getClickedBlock().getRelative(BlockFace.UP)));
            
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            
            event.setCancelled(this.plugin.searchGate(event.getClickedBlock().getRelative(event.getBlockFace())) != null);
            
        }
        
        if (this.plugin.getConfig().getBoolean("debug")) {
            
            event.getPlayer().sendMessage("§cThis block is protected as it's part of a gate.");
            
        }
        
    }
}
