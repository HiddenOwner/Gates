package net.tyrotoxism.gates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class Gate {
    
    private final List<Block> gateBlocks = new ArrayList<Block>();
    private final List<Block> solidBlocks = new ArrayList<Block>();
    
    private final Gates plugin;
    
    private final Sign sign;
    private final OfflinePlayer owner;
    private final GateType type;
    private final GateRedstone redstone;
    
    private Material material = null;
    
    public Gate(final Gates plugin, final OfflinePlayer player, final Sign sign) {
    
        this.plugin = plugin;
        this.sign = sign;
        
        GateType type = null;
        
        try {
            
            type = this.plugin.getType("default");
            
        } catch (final Exception e) {
            
            this.plugin.getLogger().log(Level.SEVERE, "The default gate type is invalid.");
            
        }
        
        this.owner = this.sign.getLine(1).isEmpty() ? player : this.plugin.getServer().getOfflinePlayer(this.sign.getLine(1));
        this.type = this.sign.getLine(2).isEmpty() ? type : this.plugin.getType(this.sign.getLine(2));
        this.redstone = this.sign.getLine(3).isEmpty() ? this.type.getRedstone() : GateRedstone.valueOf(this.sign.getLine(3));
        
        this.blockSearch(this.sign.getBlock(), this.type.getSearchRadius());
        
        if (!this.solidBlocks.isEmpty()) {
            
            final List<Block> blocks = new ArrayList<Block>();
            
            for (Block block : this.solidBlocks) {
                
                while (((block = block.getRelative(BlockFace.DOWN)).isEmpty() || block.getType().equals(this.material)) && !this.gateBlocks.contains(block)) {
                    
                    this.gateBlocks.add(block);
                    
                    if (this.solidBlocks.contains(block)) {
                        
                        blocks.add(block);
                        
                    }
                    
                }
                
            }
            
            this.solidBlocks.removeAll(blocks);
            
        }
        
    }
    
    private void blockSearch(final Block block, final int radius) {
    
        for (int x = block.getX() - radius; x < (block.getX() + radius + 1); x++) {
            
            for (int y = block.getY() - radius; y < (block.getY() + radius + 1); y++) {
                
                for (int z = block.getZ() - radius; z < (block.getZ() + radius + 1); z++) {
                    
                    final Block blockA = block.getWorld().getBlockAt(x, y, z);
                    
                    if (this.solidBlocks.contains(blockA)) {
                        
                        continue;
                        
                    }
                    
                    if (this.material == null) {
                        
                        for (final Material type : Gates.blocks) {
                            
                            if (type.equals(blockA.getType())) {
                                
                                this.material = type;
                                this.solidBlocks.add(blockA);
                                this.blockSearch(blockA, 1);
                                break;
                                
                            }
                            
                        }
                        
                    } else {
                        
                        if (this.material.equals(blockA.getType())) {
                            
                            this.solidBlocks.add(blockA);
                            this.blockSearch(blockA, 1);
                            break;
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
    public List<Block> getGateBlocks() {
    
        return this.gateBlocks;
        
    }
    
    public List<Block> getSolidBlocks() {
    
        return this.solidBlocks;
        
    }
    
    public Material getMaterial() {
    
        return this.material;
        
    }
    
    public Sign getSign() {
    
        return this.sign;
        
    }
    
    public OfflinePlayer getOwner() {
    
        return this.owner;
        
    }
    
    public GateType getType() {
    
        return this.type;
        
    }
    
    public GateRedstone getRedstone() {
    
        return this.redstone;
        
    }
    
    public boolean hasPermissionToCreate(final Player player) {
    
        return player.hasPermission("create");
        
    }
    
    public boolean hasPermissionToUse(final Player player) {
    
        return player.hasPermission("use");
        
    }
    
    public boolean hasPermissionToDestroy(final Player player) {
    
        return player.hasPermission("destroy");
        
    }
    
    public boolean isOpen() {
    
        for (final Block block : this.gateBlocks) {
            
            if (!block.isEmpty()) { return false; }
            
        }
        
        return true;
        
    }
    
    public boolean isReady() {
    
        for (final Block block : this.gateBlocks) {
            
            for (final Block blockA : this.gateBlocks) {
                
                if (!block.getType().equals(blockA.getType())) { return false; }
                
            }
            
        }
        
        return true;
        
    }
    
    public boolean isInstant() {
    
        return true;
        
    }
    
    public void open() {
    
        if (!this.isInstant()) {
            
            this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new GateTimer(this, true), 10, 10);
            
        } else {
            
            for (final Block block : this.gateBlocks) {
                
                block.setType(Material.AIR);
                
            }
            
        }
        
    }
    
    public void close() {
    
        if (!this.isInstant()) {
            
            this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new GateTimer(this, false), 10, 10);
            
        } else {
            
            for (final Block block : this.gateBlocks) {
                
                block.setType(this.material);
                
            }
            
        }
        
    }
    
    private class GateTimer implements Runnable {
        
        private final Gate gate;
        private final boolean open;
        
        public GateTimer(final Gate gate, final boolean open) {
        
            this.gate = gate;
            this.open = open;
            
        }
        
        @Override
        public void run() {
        
            // W.I.P.
            
            if (this.open) {
                
                for (final Block block : this.gate.getGateBlocks()) {
                    
                    block.setType(Material.AIR);
                    
                }
                
            } else {
                
                for (final Block block : this.gate.getGateBlocks()) {
                    
                    block.setType(this.gate.getMaterial());
                    
                }
                
            }
            
        }
    }
    
}
