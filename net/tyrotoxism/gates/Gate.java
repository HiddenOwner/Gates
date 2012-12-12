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
import org.bukkit.scheduler.BukkitRunnable;

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
        this.redstone = this.sign.getLine(3).isEmpty() ? type.getRedstone() : GateRedstone.valueOf(this.sign.getLine(3));
        
        this.blockSearch(this.sign.getBlock(), 4);
        
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
    
        return player.hasPermission("*") || player.hasPermission("gates.*") || player.hasPermission("gates.create");
        
    }
    
    public boolean hasPermissionToUse(final Player player) {
    
        if (player.hasPermission("*") || player.hasPermission("gates.*") || player.hasPermission("gates.use.*")) {
            
            return true;
            
        } else if (player.hasPermission("gates.use.player.*") || player.hasPermission(String.format("gates.use.player.%s", this.owner.getName().toLowerCase()))) {
            
            return true;
            
        } else if (player.hasPermission("gates.use.type.*") || player.hasPermission(String.format("gates.use.type.%s", this.type.getName().toLowerCase()))) {
            
            return true;
            
        } else if ((player.equals(this.owner) && player.hasPermission("gates.use.self")) || (!player.equals(this.owner) && player.hasPermission("gates.use.others"))) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }
    
    public boolean hasPermissionToDestroy(final Player player) {
    
        if (player.hasPermission("*") || player.hasPermission("gates.*") || player.hasPermission("gates.destroy.*")) {
            
            return true;
            
        } else if (player.hasPermission("gates.destroy.player.*") || player.hasPermission(String.format("gates.destroy.player.%s", this.owner.getName().toLowerCase()))) {
            
            return true;
            
        } else if (player.hasPermission("gates.destroy.type.*") || player.hasPermission(String.format("gates.destroy.type.%s", this.type.getName().toLowerCase()))) {
            
            return true;
            
        } else if ((player.equals(this.owner) && player.hasPermission("gates.destroy.self")) || (!player.equals(this.owner) && player.hasPermission("gates.destroy.others"))) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
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
    
        return this.type.getDelay() == 0;
        
    }
    
    public void open() {
    
        if (!this.isInstant()) {
            
            if (!this.plugin.isGateBusy(this)) {
                
                this.plugin.getBusyGates().add(this.gateBlocks);
                this.plugin.getServer().getScheduler().runTaskLater(this.plugin, new GateTimer(true), this.type.getDelay());
                
            }
            
        } else {
            
            for (final Block block : this.gateBlocks) {
                
                block.setType(Material.AIR);
                
            }
            
        }
        
    }
    
    public void close() {
    
        if (!this.isInstant()) {
            
            if (!this.plugin.isGateBusy(this)) {
                
                this.plugin.getBusyGates().add(this.gateBlocks);
                this.plugin.getServer().getScheduler().runTaskLater(this.plugin, new GateTimer(false), this.type.getDelay());
                
            }
            
        } else {
            
            for (final Block block : this.gateBlocks) {
                
                block.setType(this.material);
                
            }
            
        }
        
    }
    
    private class GateTimer extends BukkitRunnable {
        
        private final boolean open;
        
        public GateTimer(final boolean open) {
        
            this.open = open;
            
        }
        
        @Override
        public void run() {
        
            if (this.open) {
                
                int bottom = Gate.this.sign.getWorld().getMaxHeight();
                
                for (final Block block : Gate.this.gateBlocks) {
                    
                    if ((block.getY() < bottom) && block.getType().equals(Gate.this.material)) {
                        
                        bottom = block.getY();
                        
                    }
                    
                }
                
                for (final Block block : Gate.this.gateBlocks) {
                    
                    if (block.getY() == bottom) {
                        
                        block.setType(Material.AIR);
                        
                    }
                    
                }
                
            } else {
                
                int top = 0;
                
                for (final Block block : Gate.this.gateBlocks) {
                    
                    if ((block.getY() > top) && !block.getType().equals(Gate.this.material)) {
                        
                        top = block.getY();
                        
                    }
                    
                }
                
                for (final Block block : Gate.this.gateBlocks) {
                    
                    if (block.getY() == top) {
                        
                        block.setType(Gate.this.material);
                        
                    }
                    
                }
                
            }
            
            if (!Gate.this.isReady()) {
                
                Gate.this.plugin.getServer().getScheduler().runTaskLater(Gate.this.plugin, new GateTimer(this.open), Gate.this.type.getDelay());
                
            } else {
                
                Gate.this.plugin.getBusyGates().remove(Gate.this.gateBlocks);
                
            }
            
        }
        
    }
    
}
