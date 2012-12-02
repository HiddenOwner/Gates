package net.tyrotoxism.gates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.tyrotoxism.gates.listener.GateListener;
import net.tyrotoxism.gates.listener.ProtectListener;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

public class Gates extends JavaPlugin {
    
    public static final String label = "[Gate]";
    private final List<GateType> types = new ArrayList<GateType>();
    
    @Override
    public void onEnable() {
    
        this.types.add(new GateType("default", 32, 32, GateRedstone.OFF));
        this.types.add(new GateType("instant", 0, 0, GateRedstone.OFF));
        this.types.add(new GateType("redstone", 32, 32, GateRedstone.ON));
        
        this.getServer().getPluginManager().registerEvents(new GateListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ProtectListener(), this);
        
        this.getLogger().log(Level.INFO, String.format("%s enabled.", this.getDescription().getFullName()));
        
    }
    
    public GateType getType(final String name) {
    
        for (final GateType type : this.types) {
            
            if (type.getName().equalsIgnoreCase(name)) { return type; }
            
        }
        
        return null;
        
    }
    
    public Gate getGate(final Block block, final OfflinePlayer player) {
    
        if (!(block.getState() instanceof Sign)) { return null; }
        
        final Sign sign = (Sign) block.getState();
        
        boolean isGate = false;
        
        for (final String line : sign.getLines()) {
            
            if (line.equalsIgnoreCase(Gates.label)) {
                
                isGate = true;
                break;
                
            }
            
        }
        
        if (!isGate) { return null; }
        
        GateType type = null;
        
        try {
            
            type = this.getType("default");
            
        } catch (final Exception e) {
            
            this.getLogger().log(Level.SEVERE, "The default gate type is invalid.");
            return null;
            
        }
        
        final Gate gate = new Gate(this, sign);
        
        sign.setLine(0, Gates.label);
        sign.setLine(1, sign.getLine(1).isEmpty() ? player.getName() : gate.getOwner().getName());
        sign.setLine(2, sign.getLine(2).isEmpty() ? type.getName() : gate.getType().getName());
        sign.setLine(3, sign.getLine(3).isEmpty() ? type.getRedstone().name() : gate.getRedstone().name());
        
        sign.update();
        
        return gate;
        
    }
}
