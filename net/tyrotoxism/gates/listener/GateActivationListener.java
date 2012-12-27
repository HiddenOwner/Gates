package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.GateRedstone;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateActivationEvent;
import net.tyrotoxism.gates.event.GateActivationEvent.ActivationAction;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GateActivationListener implements Listener {
    
    private final Gates plugin;
    
    public GateActivationListener(final Gates plugin) {
    
        this.plugin = plugin;
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateSignInteraction(final PlayerInteractEvent event) {
    
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        
        final Gate gate = this.plugin.getGate(event.getClickedBlock());
        final Player player = event.getPlayer();
        
        if (gate == null) { return; }
        
        event.setCancelled(true);
        
        final GateActivationEvent evt = new GateActivationEvent(gate, player, ActivationAction.PLAYER);
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (evt.isCancelled() || this.plugin.isGateBusy(gate) || !gate.hasPermissionToUse(player)) { return; }
        
        if (!gate.isOpen()) {
            
            gate.open();
            
        } else {
            
            gate.close();
            
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateSignRedstone(final BlockRedstoneEvent event) {
    
        final Gate gate = this.plugin.getGate(event.getBlock(), null);
        
        if (gate == null) { return; }
        
        final GateActivationEvent evt = new GateActivationEvent(gate, null, ActivationAction.REDSTONE);
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (evt.isCancelled() || this.plugin.isGateBusy(gate) || gate.getRedstone().equals(GateRedstone.OFF)) { return; }
        
        if (gate.getRedstone().equals(GateRedstone.ON)) {
            
            if (event.getBlock().isBlockPowered()) {
                
                if (!gate.isOpen()) {
                    
                    gate.open();
                    
                }
                
            } else {
                
                if (gate.isOpen()) {
                    
                    gate.close();
                    
                }
                
            }
            
        } else if (gate.getRedstone().equals(GateRedstone.TOGGLE)) {
            
            if (!gate.isOpen()) {
                
                gate.open();
                
            } else {
                
                gate.close();
                
            }
            
        }
        
    }
    
}
