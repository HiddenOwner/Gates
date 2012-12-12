package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateDestructionEvent;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GateDestructionListener implements Listener {
    
    private final Gates plugin;
    
    public GateDestructionListener(final Gates plugin) {
    
        this.plugin = plugin;
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateSignBreak(final BlockBreakEvent event) {
    
        final Gate gate = this.plugin.getGate(event.getBlock());
        final Player player = event.getPlayer();
        
        if (gate == null) { return; }
        
        final GateDestructionEvent evt = new GateDestructionEvent(gate, player);
        
        evt.setCancelled(!gate.hasPermissionToDestroy(player));
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        event.setCancelled(evt.isCancelled());
        
        if (!evt.isCancelled()) {
            
            for (final Block block : gate.getGateBlocks()) {
                
                block.setType(Material.AIR);
                
            }
            
        }
        
    }
    
}
