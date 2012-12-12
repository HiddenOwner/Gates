package net.tyrotoxism.gates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.tyrotoxism.gates.listener.GateActivationListener;
import net.tyrotoxism.gates.listener.GateCreationListener;
import net.tyrotoxism.gates.listener.GateDestructionListener;
import net.tyrotoxism.gates.listener.GateProtectionListener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Gates extends JavaPlugin {
    
    public static final String label = "[Gate]";
    public static final Material[] blocks = new Material[] { Material.FENCE, Material.NETHER_FENCE, Material.THIN_GLASS, Material.IRON_FENCE };
    
    private List<GateType> types;
    private List<List<Block>> busyGates;
    
    @Override
    public void onEnable() {
    
        this.types = new ArrayList<GateType>();
        this.busyGates = new ArrayList<List<Block>>();
        
        this.types.add(new GateType("default", 16, GateRedstone.OFF, 16, false));
        
        this.getServer().getPluginManager().registerEvents(new GateActivationListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GateCreationListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GateDestructionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GateProtectionListener(this), this);
        
        this.getLogger().log(Level.INFO, String.format("%s enabled.", this.getDescription().getFullName()));
        
    }
    
    public GateType getType(final String name) {
    
        for (final GateType type : this.types) {
            
            if (type.getName().equalsIgnoreCase(name)) { return type; }
            
        }
        
        return null;
        
    }
    
    public boolean isGateBusy(final Gate gate) {
    
        for (final List<Block> blocks : this.busyGates) {
            
            if (gate.getGateBlocks().equals(blocks)) { return true; }
            
        }
        
        return false;
        
    }
    
    public List<List<Block>> getBusyGates() {
    
        return this.busyGates;
        
    }
    
    public Gate getGate(final Block block) {
    
        return this.getGate(block, null);
        
    }
    
    public Gate getGate(final Block block, final String[] lines) {
    
        return this.getGate(block, null, lines);
        
    }
    
    public Gate getGate(final Block block, final Player player, final String[] lines) {
    
        if (!(block.getState() instanceof Sign)) { return null; }
        
        final Sign sign = (Sign) block.getState();
        
        for (final String line : (lines == null ? sign.getLines() : lines)) {
            
            if (line.equalsIgnoreCase(Gates.label)) { return new Gate(this, player, sign); }
            
        }
        
        return null;
        
    }
    
}
