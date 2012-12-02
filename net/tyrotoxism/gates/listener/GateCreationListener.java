package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;

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
    public void onSignChange(final SignChangeEvent event) {
    
        final Gate gate = this.plugin.getGate(event.getBlock(), event.getPlayer());
        
        if (gate == null) { return; }
        
    }
    
}
