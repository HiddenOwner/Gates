package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateCreationEvent;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class GateCreationListener implements Listener {
    
    private final Gates plugin;
    
    public GateCreationListener(final Gates plugin) {
    
        this.plugin = plugin;
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateSignChange(final SignChangeEvent event) {
    
        if (event.isCancelled()) { return; }
        
        final Gate gate = this.plugin.getGate(event.getBlock(), event.getPlayer(), event.getLines());
        
        if ((gate == null) || !gate.hasPermissionToCreate(event.getPlayer())) { return; }
        
        final GateCreationEvent evt = new GateCreationEvent(gate, event.getPlayer());
        final int radius = this.plugin.getConfig().getInt("search-radius");
        
        for (int x = event.getBlock().getX() - radius; !evt.isCancelled() && (x <= (event.getBlock().getX() + radius)); x++) {
            
            for (int y = event.getBlock().getY() - radius; !evt.isCancelled() && (y <= (event.getBlock().getY() + radius)); y++) {
                
                for (int z = event.getBlock().getZ() - radius; !evt.isCancelled() && (z <= (event.getBlock().getZ() + radius)); z++) {
                    
                    final Block blockA = event.getBlock().getWorld().getBlockAt(x, y, z);
                    
                    if (Gates.blocks.contains(blockA.getType()) || Gates.empty.contains(blockA.getType())) {
                        
                        final Gate gateA = this.plugin.searchGate(blockA);
                        
                        if ((gateA != null) && !(event.getPlayer().hasPermission("gates.*") || event.getPlayer().hasPermission("gates.op") || (gateA.getOwner() == event.getPlayer()))) {
                            
                            evt.setCancelled(true);
                            
                            if (this.plugin.getConfig().getBoolean("debug")) {
                                
                                event.getPlayer().sendMessage("§cAnother gate already exists here.");
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (evt.isCancelled()) {
            
            event.setCancelled(true);
            
            if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                
                event.getBlock().setType(Material.AIR);
                
            } else {
                
                event.getBlock().breakNaturally();
                
            }
            
            return;
            
        }
        
        event.setLine(0, Gates.label);
        event.setLine(1, gate.getOwner().getName());
        event.setLine(2, gate.getType().getName());
        event.setLine(3, gate.getRedstone().name());
        
        if (this.plugin.getConfig().getBoolean("debug")) {
            
            event.getPlayer().sendMessage("§aGate created.");
            
        }
        
    }
    
}
