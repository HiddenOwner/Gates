package net.tyrotoxism.gates.listener;

import java.util.logging.Level;

import net.tyrotoxism.gates.Gate;
import net.tyrotoxism.gates.GateType;
import net.tyrotoxism.gates.Gates;
import net.tyrotoxism.gates.event.GateCreationEvent;

import org.bukkit.block.Sign;
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
    
        final Gate gate = this.plugin.getGate(event.getBlock(), event.getLines());
        final Player player = event.getPlayer();
        
        if ((gate == null) || !gate.hasPermissionToCreate(player)) { return; }
        
        GateType type = null;
        
        try {
            
            type = this.plugin.getType("default");
            
        } catch (final Exception e) {
            
            this.plugin.getLogger().log(Level.SEVERE, "The default gate type is invalid.");
            event.setCancelled(true);
            return;
            
        }
        
        final GateCreationEvent evt = new GateCreationEvent(gate, event.getPlayer());
        
        this.plugin.getServer().getPluginManager().callEvent(evt);
        
        if (evt.isCancelled()) {
            
            event.setCancelled(true);
            return;
            
        }
        
        final Sign sign = gate.getSign();
        
        event.setLine(0, Gates.label);
        event.setLine(1, sign.getLine(1).isEmpty() ? player.getName() : gate.getOwner().getName());
        event.setLine(2, sign.getLine(2).isEmpty() ? type.getName() : gate.getType().getName());
        event.setLine(3, sign.getLine(3).isEmpty() ? type.getRedstone().name() : gate.getRedstone().name());
        
        player.sendMessage("§aGate successfully created.");
        
    }
    
}
