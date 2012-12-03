package net.tyrotoxism.gates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.tyrotoxism.gates.listener.GateActivationListener;
import net.tyrotoxism.gates.listener.GateCreationListener;
import net.tyrotoxism.gates.listener.GateDestructionListener;

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
        
        this.getServer().getPluginManager().registerEvents(new GateActivationListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GateCreationListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GateDestructionListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new GateProtectionListener(this), this);
        
        this.getLogger().log(Level.INFO, String.format("%s enabled.", this.getDescription().getFullName()));
        
    }
    
    public GateType getType(final String name) {
    
        for (final GateType type : this.types) {
            
            if (type.getName().equalsIgnoreCase(name)) { return type; }
            
        }
        
        return null;
        
    }
    
    public Gate getGate(final Block block) {
    
        return this.getGate(block, null);
        
    }
    
    public Gate getGate(final Block block, final String[] lines) {
    
        if (!(block.getState() instanceof Sign)) { return null; }
        
        final Sign sign = (Sign) block.getState();
        
        for (final String line : (lines == null ? sign.getLines() : lines)) {
            
            if (line.equalsIgnoreCase(Gates.label)) { return new Gate(this, sign); }
            
        }
        
        return null;
        
    }
    
}
