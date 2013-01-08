package net.tyrotoxism.gates.listener;

import java.util.logging.Level;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.GateRedstone;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateActivationEvent;
import net.tyrotoxism.gates.event.GateActivationEvent.ActivationAction;

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
    
        if (event.isCancelled() || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        
        final Gate gate = this.plugin.getGate(event.getClickedBlock());
        
        if (gate == null) { return; }
        
        event.setCancelled(true);
        
        final GateActivationEvent evt = new GateActivationEvent(gate, event.getPlayer(), ActivationAction.PLAYER);
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (evt.isCancelled() || this.plugin.isGateBusy(gate) || !gate.hasPermissionToUse(event.getPlayer())) { return; }
        
        if (!gate.isOpen()) {
            
            gate.open();
            
            if (this.plugin.getConfig().getBoolean("debug")) {
                
                event.getPlayer().sendMessage("§eOpening gate.");
                
            }
            
            if (this.plugin.getConfig().getBoolean("console-log")) {
                
                this.plugin.getLogger().log(Level.INFO, String.format("%s opened gate %s", event.getPlayer().getName(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                
            }
            
        } else {
            
            gate.close();
            
            if (this.plugin.getConfig().getBoolean("debug")) {
                
                event.getPlayer().sendMessage("§eClosing gate.");
                
            }
            
            if (this.plugin.getConfig().getBoolean("console-log")) {
                
                this.plugin.getLogger().log(Level.INFO, String.format("%s closed gate %s", event.getPlayer().getName(), String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                
            }
            
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGateSignRedstone(final BlockRedstoneEvent event) {
    
        final Gate gate = this.plugin.getGate(event.getBlock());
        
        if (gate == null) { return; }
        
        final GateActivationEvent evt = new GateActivationEvent(gate, null, ActivationAction.REDSTONE);
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (evt.isCancelled() || this.plugin.isGateBusy(gate) || gate.getRedstone().equals(GateRedstone.OFF)) { return; }
        
        if (gate.getRedstone().equals(GateRedstone.ON)) {
            
            if (event.getBlock().isBlockPowered()) {
                
                if (!gate.isOpen()) {
                    
                    gate.open();
                    
                }
                
                if (this.plugin.getConfig().getBoolean("console-log")) {
                    
                    this.plugin.getLogger().log(Level.INFO, String.format("Redstone opened gate %s", String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                    
                }
                
            } else {
                
                if (gate.isOpen()) {
                    
                    gate.close();
                    
                }
                
                if (this.plugin.getConfig().getBoolean("console-log")) {
                    
                    this.plugin.getLogger().log(Level.INFO, String.format("Redstone closed gate %s", String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                    
                }
                
            }
            
        } else if (gate.getRedstone().equals(GateRedstone.TOGGLE)) {
            
            if (!gate.isOpen()) {
                
                gate.open();
                
                if (this.plugin.getConfig().getBoolean("console-log")) {
                    
                    this.plugin.getLogger().log(Level.INFO, String.format("Redstone opened gate %s", String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                    
                }
                
            } else {
                
                gate.close();
                
                if (this.plugin.getConfig().getBoolean("console-log")) {
                    
                    this.plugin.getLogger().log(Level.INFO, String.format("Redstone closed gate %s", String.format("(GATE SIGN x%s y%s z%s)", gate.getSign().getX(), gate.getSign().getY(), gate.getSign().getZ())));
                    
                }
                
            }
            
        }
        
    }
    
}
