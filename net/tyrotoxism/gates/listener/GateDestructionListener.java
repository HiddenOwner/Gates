package net.tyrotoxism.gates.listener;

import java.util.logging.Level;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateDestructionEvent;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
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
    
        if (event.isCancelled()) { return; }
        
        Gate gate = this.plugin.getGate(event.getBlock());
        
        if (gate == null) {
            
            for (final BlockFace face : new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP }) {
                
                final Block block = event.getBlock().getRelative(face);
                
                if ((block.getState() instanceof Sign) && ((org.bukkit.material.Sign) block.getState().getData()).getAttachedFace().equals(face.getOppositeFace())) {
                    
                    gate = this.plugin.getGate(block);
                    
                    if (gate != null) {
                        
                        break;
                        
                    }
                    
                }
                
            }
            
        }
        
        if (gate == null) {
            
            return;
            
        } else if (this.plugin.isGateBusy(gate)) {
            
            event.setCancelled(true);
            return;
            
        }
        
        final GateDestructionEvent evt = new GateDestructionEvent(gate, event.getPlayer());
        
        evt.setCancelled(!gate.hasPermissionToDestroy(event.getPlayer()));
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (!evt.isCancelled()) {
            
            for (final Block block : gate.getGateBlocks()) {
                
                block.setType(Material.AIR);
                
            }
            
            if (this.plugin.getConfig().getBoolean("debug")) {
                
                event.getPlayer().sendMessage("§eGate removed.");
                
            }
            
            if (this.plugin.getConfig().getBoolean("console-log")) {
                
                this.plugin.getLogger().log(Level.INFO, String.format("%s removed gate sign %s", event.getPlayer(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                
            }
            
        } else {
            
            if (this.plugin.getConfig().getBoolean("debug")) {
                
                event.getPlayer().sendMessage("§cYou can't break that gate sign.");
                
            }
            
            if (this.plugin.getConfig().getBoolean("console-log")) {
                
                this.plugin.getLogger().log(Level.INFO, String.format("%s tried to remove gate sign %s, but was denied", event.getPlayer(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                
            }
            
        }
        
    }
    
}
