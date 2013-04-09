package com.geekzmultimedia.gates.listener;

import java.util.List;
import java.util.logging.Level;

import com.geekzmultimedia.gates.Gate;
import com.geekzmultimedia.gates.Gates;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
    public void onGateBlockProtectBreak(final BlockBreakEvent event) {
    
        if (event.isCancelled()) { return; }
        
        final Gate gate = Gates.blocks.contains(event.getBlock().getType()) ? this.plugin.searchGate(event.getBlock()) : this.plugin.searchGate(event.getBlock().getRelative(BlockFace.UP));
        
        if ((gate != null) && (this.plugin.getGate(event.getBlock()) == null) && (gate.getGateBlocks().contains(event.getBlock()) || gate.getSolidBlocks().contains(event.getBlock()) || gate.getGateBlocks().contains(event.getBlock().getRelative(BlockFace.UP)) || gate.getSolidBlocks().contains(event.getBlock().getRelative(BlockFace.UP)))) {
            
            event.setCancelled(true);
            
            if (this.plugin.getConfig().getBoolean("debug")) {
                
                event.getPlayer().sendMessage("§cThis block is protected as it's part of a gate.");
                
            }
            
            if (this.plugin.getConfig().getBoolean("console-log")) {
                
                this.plugin.getLogger().log(Level.INFO, String.format("%s tried to damage a gate block %s, but was denied", event.getPlayer(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                
            }
            
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateBlockProtectPlace(final BlockPlaceEvent event) {
    
        if (event.isCancelled()) { return; }
        
        Gate gate = this.plugin.searchGate(event.getBlock());
        
        if (Gates.blocks.contains(event.getItemInHand().getType())) {
            
            if (gate == null) {
                
                Block block = event.getBlock();
                
                while ((gate == null) && Gates.empty.contains((block = block.getRelative(BlockFace.UP)).getType())) {
                    
                    for (int x = block.getX() - 1; (gate == null) && (x <= (block.getX() + 1)); x++) {
                        
                        for (int z = block.getZ() - 1; (gate == null) && (z <= (block.getZ() + 1)); z++) {
                            
                            gate = this.plugin.searchGate(block.getWorld().getBlockAt(x, block.getY(), z));
                            
                        }
                        
                    }
                    
                }
                
            }
            
            if (gate != null) {
                
                final List<Block> blocks = gate.getSolidBlocks();
                boolean droids = false;
                
                blocks.addAll(gate.getGateBlocks());
                
                for (final Block blockA : blocks) {
                    
                    for (int xA = blockA.getX() - 1; !droids && (xA <= (blockA.getX() + 1)); xA++) {
                        
                        for (int yA = blockA.getY() - 1; !droids && (yA <= (blockA.getY() + 1)); yA++) {
                            
                            for (int zA = blockA.getZ() - 1; !droids && (zA <= (blockA.getZ() + 1)); zA++) {
                                
                                if (event.getBlock().equals(blockA.getWorld().getBlockAt(xA, yA, zA))) {
                                    
                                    droids = true; // This is the droids we are looking for
                                    
                                }
                                
                            }
                            
                        }
                        
                    }
                    
                    if (droids) {
                        
                        break;
                        
                    }
                    
                }
                
                if (!droids) {
                    
                    gate = null;
                    
                }
                
            }
            
            if (gate == null) {
                
                final int radius = this.plugin.getConfig().getInt("search-radius");
                
                for (int x = event.getBlock().getX() - radius; (gate == null) && (x <= (event.getBlock().getX() + radius)); x++) {
                    
                    for (int y = event.getBlock().getY() - radius; (gate == null) && (y <= (event.getBlock().getY() + radius)); y++) {
                        
                        for (int z = event.getBlock().getZ() - radius; (gate == null) && (z <= (event.getBlock().getZ() + radius)); z++) {
                            
                            gate = this.plugin.getGate(event.getBlock().getWorld().getBlockAt(x, y, z));
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        if (gate != null) {
            
            event.setCancelled(true);
            
            if (this.plugin.getConfig().getBoolean("debug")) {
                
                event.getPlayer().sendMessage("§cThis block is protected as it's part of a gate.");
                
            }
            
            if (this.plugin.getConfig().getBoolean("console-log")) {
                
                this.plugin.getLogger().log(Level.INFO, String.format("%s tried to modify a gate block %s, but was denied", event.getPlayer(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                
            }
            
        }
        
    }
    
}
