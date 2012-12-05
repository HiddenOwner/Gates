package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateCreationEvent;

import org.bukkit.entity.Player;
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
    
        final Gate gate = this.plugin.getGate(event.getBlock(), event.getPlayer(), event.getLines());
        final Player player = event.getPlayer();
        
        if ((gate == null) || !gate.hasPermissionToCreate(player)) { return; }
        
        final GateCreationEvent evt = new GateCreationEvent(gate, event.getPlayer());
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (evt.isCancelled()) {
            
            event.setCancelled(true);
            return;
            
        }
        
        event.setLine(0, Gates.label);
        event.setLine(1, gate.getOwner().getName());
        event.setLine(2, gate.getType().getName());
        event.setLine(3, gate.getRedstone().name());
        
    }
    
}
