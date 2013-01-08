package net.tyrotoxism.gates.listener;

import java.util.logging.Level;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;

import org.bukkit.block.Block;
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
            
            final Gate gate = this.plugin.searchGate(event.getClickedBlock());
            
            if ((gate != null) && (this.plugin.getGate(event.getClickedBlock()) == null) && (gate.getGateBlocks().contains(event.getClickedBlock()) || gate.getSolidBlocks().contains(event.getClickedBlock()) || gate.getGateBlocks().contains(event.getClickedBlock().getRelative(BlockFace.UP)) || gate.getSolidBlocks().contains(event.getClickedBlock().getRelative(BlockFace.UP)))) {
                
                event.setCancelled(true);
                
                if (this.plugin.getConfig().getBoolean("debug")) {
                    
                    event.getPlayer().sendMessage("§cThis block is protected as it's part of a gate.");
                    
                }
                
                if (this.plugin.getConfig().getBoolean("console-log")) {
                    
                    this.plugin.getLogger().log(Level.INFO, String.format("%s tried to damage a gate block %s, but was denied", event.getPlayer(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                    
                }
                
            }
            
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            
            final Block block = event.getClickedBlock().getRelative(event.getBlockFace());
            final int radius = this.plugin.getConfig().getInt("search-radius");
            Gate gate = this.plugin.searchGate(block);
            boolean finished = false;
            
            if (Gates.blocks.contains(block.getType())) {
                
                for (int x = block.getX() - radius; x <= (block.getX() + radius); x++) {
                    
                    for (int y = block.getY() - radius; y <= (block.getY() + radius); y++) {
                        
                        for (int z = block.getZ() - radius; z <= (block.getZ() + radius); z++) {
                            
                            final Block blockA = block.getWorld().getBlockAt(x, y, z);
                            gate = this.plugin.getGate(blockA);
                            
                            if ((gate == null) && Gates.blocks.contains(blockA.getType()) && (x <= (block.getX() + 1)) && (x >= (block.getX() - 1)) && (y <= (block.getY() + 1)) && (y >= (block.getY() - 1)) && (z <= (block.getZ() + 1)) && (z >= (block.getZ() - 1))) {
                                
                                gate = this.plugin.searchGate(blockA);
                                
                            } else {
                                
                                Block blockB = blockA.getRelative(BlockFace.DOWN);
                                
                                while (Gates.blocks.contains((blockB = blockB.getRelative(BlockFace.UP)).getType()) || Gates.empty.contains(blockB.getType())) {
                                    
                                    for (int xA = blockB.getX() - radius; xA <= (blockB.getX() + radius); xA++) {
                                        
                                        for (int zA = blockB.getZ() - radius; zA <= (blockB.getZ() + radius); zA++) {
                                            
                                            final Block blockC = blockB.getWorld().getBlockAt(xA, blockB.getY(), zA);
                                            
                                            gate = this.plugin.searchGate(blockC);
                                            
                                            if (gate != null) {
                                                
                                                finished = true;
                                                break;
                                                
                                            }
                                            
                                        }
                                        
                                        if (finished) {
                                            
                                            break;
                                            
                                        }
                                        
                                    }
                                    
                                    if (finished) {
                                        
                                        break;
                                        
                                    }
                                    
                                }
                                
                            }
                            
                            if (gate != null) {
                                
                                finished = true;
                                break;
                                
                            }
                            
                        }
                        
                        if (finished) {
                            
                            break;
                            
                        }
                        
                    }
                    
                    if (finished) {
                        
                        break;
                        
                    }
                    
                }
                
            }
            
            if (gate != null) {
                
                event.setCancelled(true);
                
                if (this.plugin.getConfig().getBoolean("debug")) {
                    
                    event.getPlayer().sendMessage("§cThis block is protected as it's part of a gate.");
                    
                }
                
                if (this.plugin.getConfig().getBoolean("console-log")) {
                    
                    this.plugin.getLogger().log(Level.INFO, String.format("%s tried to damage a gate block %s, but was denied", event.getPlayer(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                    
                }
                
            }
            
        }
        
    }
}
