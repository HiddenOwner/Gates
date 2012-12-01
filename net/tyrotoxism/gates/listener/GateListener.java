package net.tyrotoxism.gates.listener;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.GateRedstone;
import net.tyrotoxism.gates.GateType;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateActivationEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GateListener implements Listener {
    
    private final Gates plugin;
    
    public GateListener(final Gates plugin) {
    
        this.plugin = plugin;
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(final PlayerInteractEvent event) {
    
        final Gate gate = this.plugin.getGate(event.getClickedBlock(), event.getPlayer(), new GateType("test", 0, 0, "permission"), GateRedstone.OFF);
        
        if (gate == null) { return; }
        
        final GateActivationEvent eventA = new GateActivationEvent(gate, event.getPlayer());
        
        this.plugin.getServer().getPluginManager().callEvent(eventA);
        
        if (eventA.isCancelled() || !gate.isReady()) { return; }
        
        if (gate.isOpen()) {
            
            gate.open(event.getPlayer());
            
        } else {
            
            gate.close(event.getPlayer());
            
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreakEvent(final BlockBreakEvent event) {
    
        // Check if gate (or gate blocks) and if permitted
        
    }
    
}
