package net.tyrotoxism.gates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.tyrotoxism.gates.listener.GateActivationListener;
import net.tyrotoxism.gates.listener.GateCreationListener;
import net.tyrotoxism.gates.listener.GateDestructionListener;
import net.tyrotoxism.gates.listener.GateProtectionListener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Gates extends JavaPlugin {
    
    public static final String label = "[Gate]";
    public static final List<Material> blocks = Arrays.asList(new Material[] { Material.FENCE, Material.NETHER_FENCE, Material.THIN_GLASS, Material.IRON_FENCE });
    public static final List<Material> empty = Arrays.asList(new Material[] { Material.AIR, Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER });
    public static final List<Sign> busyGates = new ArrayList<Sign>();
    
    private List<GateType> types;
    
    @Override
    public void onEnable() {
    
        this.types = new ArrayList<GateType>();
        
        this.getDataFolder().mkdirs();
        
        final File file = new File(this.getDataFolder(), "config.yml");
        boolean loaded = false;
        
        while (!loaded) {
            
            try {
                
                this.getConfig().load(file);
                
            } catch (final FileNotFoundException u2silly) {
                
                try {
                    
                    file.createNewFile();
                    
                } catch (final IOException e) {
                    
                    e.printStackTrace();
                    
                }
                
                try {
                    
                    final InputStream input = this.getClass().getResource("/config.yml").openStream();
                    final OutputStream output = new FileOutputStream(file);
                    int read;
                    
                    while (0 <= (read = input.read())) {
                        
                        output.write(read);
                        
                    }
                    
                    output.close();
                    input.close();
                    
                } catch (final FileNotFoundException e) {
                    
                    final boolean deleted = file.delete();
                    
                    this.getLogger().log(Level.SEVERE, String.format("%sestart (not reload) the server.", deleted ? "R" : "Delete the empty \"plugins/Gates/config.yml\" file and r"));
                    e.printStackTrace();
                    
                } catch (final IOException e) {
                    
                    e.printStackTrace();
                    
                } finally {
                    
                    this.getLogger().log(Level.INFO, "The configuration file has been created.");
                    
                }
                
            } catch (final IOException e) {
                
                e.printStackTrace();
                
            } catch (final InvalidConfigurationException e) {
                
                e.printStackTrace();
                
            } finally {
                
                loaded = true;
                
            }
            
        }
        
        final ConfigurationSection parent = this.getConfig().getConfigurationSection("type");
        
        for (final String type : parent.getKeys(false)) {
            
            final ConfigurationSection section = parent.getConfigurationSection(type);
            
            this.types.add(new GateType(type, section.getInt("delay"), GateRedstone.getByName(section.getString("redstone"))));
            
        }
        
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
    
        if (Gates.busyGates.isEmpty() || gate.getSolidBlocks().isEmpty()) { return false; }
        
        for (final Sign sign : Gates.busyGates) {
            
            if (this.searchGate(gate.getSolidBlocks().get(0)).getSign().equals(sign)) { return true; }
            
        }
        
        return false;
        
    }
    
    public Gate getGate(final Block block) {
    
        return this.getGate(block, null, null);
        
    }
    
    public Gate getGate(final Block block, final Player player) {
    
        return this.getGate(block, player, null);
        
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
    
    public Gate searchGate(final Block block) {
    
        return new GateSearch(this, block).getGate();
        
    }
    
}
