package net.tyrotoxism.gates;

import java.util.HashMap;
import java.util.logging.Level;

import net.tyrotoxism.gates.listener.GateListener;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

public class Gates extends JavaPlugin {
    
    private final String string = "[Gate]";
    private final HashMap<String, GateType> types = new HashMap<String, GateType>();
    
    @Override
    public void onEnable() {
    
        this.getServer().getPluginManager().registerEvents(new GateListener(this), this);
        this.getLogger().log(Level.INFO, String.format("%s enabled.", this.getDescription().getFullName()));
        
    }
    
    public String getString() {
    
        return this.string;
        
    }
    
    public GateType getType(final String name) {
    
        return this.types.containsKey(name) ? this.types.get(name) : null;
        
    }
    
    public Gate getGate(final Block block, final OfflinePlayer player, final GateType type, final GateRedstone redstone) {
    
        if (!(block.getState() instanceof Sign)) { return null; }
        
        final Sign sign = (Sign) block.getState();
        boolean isGate = false;
        
        for (final String line : sign.getLines()) {
            
            if (line.equalsIgnoreCase(this.getString())) {
                
                isGate = true;
                break;
                
            }
            
        }
        
        if (!isGate) { return null; }
        
        final Gate gate = new Gate(this, sign);
        
        sign.setLine(0, this.getString());
        sign.setLine(1, gate.getOwner(player).getName());
        sign.setLine(2, gate.getType(type).getName());
        sign.setLine(3, gate.getRedstone(redstone).name());
        
        sign.update();
        
        return gate;
        
    }
}
