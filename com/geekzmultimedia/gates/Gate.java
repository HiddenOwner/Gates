package com.geekzmultimedia.gates;

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
            
            type = this.plugin.getType(this.plugin.getConfig().getString("default-type"));
            
        } catch (final Exception e) {
            
            this.plugin.getLogger().log(Level.SEVERE, "The default gate type is invalid. Check your configurations.");
            e.printStackTrace();
            
        }
        
        this.owner = this.sign.getLine(1).isEmpty() ? player : this.plugin.getServer().getOfflinePlayer(this.sign.getLine(1));
        this.type = this.sign.getLine(2).isEmpty() ? type : this.plugin.getType(this.sign.getLine(2));
        this.redstone = this.sign.getLine(3).isEmpty() ? type.getRedstone() : GateRedstone.valueOf(this.sign.getLine(3));
        
        try {
            
            this.searchBlocks(this.sign.getBlock(), this.plugin.getConfig().getInt("search-radius"), true);
            
        } catch (final Exception e) {
            
            this.plugin.getLogger().log(Level.SEVERE, "The search radius is invalid. Check your configurations.");
            e.printStackTrace();
            
        }
        
        if (!this.solidBlocks.isEmpty()) {
            
            final List<Block> blocks = new ArrayList<Block>();
            
            for (Block block : this.solidBlocks) {
                
                while (Gates.empty.contains((block = block.getRelative(BlockFace.DOWN)).getType()) || (block.getType().equals(this.material) && !this.gateBlocks.contains(block))) {
                    
                    this.gateBlocks.add(block);
                    
                    if (this.solidBlocks.contains(block)) {
                        
                        blocks.add(block);
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
    private void searchBlocks(final Block block, final int radius, final boolean origin) {
    
        final int maximumSize = this.plugin.getConfig().getInt("maximum-size");
        
        for (int x = block.getX() - radius; (this.solidBlocks.size() < maximumSize) && (x <= (block.getX() + radius)); x++) {
            
            for (int y = block.getY() - radius; (this.solidBlocks.size() < maximumSize) && (y <= (block.getY() + radius)); y++) {
                
                for (int z = block.getZ() - radius; (this.solidBlocks.size() < maximumSize) && (z <= (block.getZ() + radius)); z++) {
                    
                    Block blockA = block.getWorld().getBlockAt(x, y, z);
                    
                    if (((this.material != null) && this.material.equals(blockA.getType())) || Gates.blocks.contains(blockA.getType())) {
                        
                        if ((this.solidBlocks.size() > 1) && !this.plugin.getConfig().getBoolean("branches") && !(((this.solidBlocks.get(0).getX() == x) && (this.solidBlocks.get(1).getX() == x)) || ((this.solidBlocks.get(0).getZ() == z) && (this.solidBlocks.get(1).getZ() == z)))) {
                            
                            continue;
                            
                        }
                        
                        if (this.material == null) {
                            
                            this.material = blockA.getType();
                            
                        }
                        
                        while (this.material.equals(blockA.getRelative(BlockFace.UP).getType())) {
                            
                            blockA = blockA.getRelative(BlockFace.UP);
                            
                        }
                        
                        if (!this.solidBlocks.contains(blockA)) {
                            
                            this.solidBlocks.add(blockA);
                            this.searchBlocks(blockA, 1, false);
                            
                            if (origin) { return; }
                            
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
    
        return !this.plugin.getConfig().getBoolean("permissions") || player.hasPermission("*") || player.hasPermission("gates.all") || player.hasPermission("gates.create")|| player.hasPermission("gates.player");
        
    }
    
    public boolean hasPermissionToUse(final Player player) {
    
        if (!this.plugin.getConfig().getBoolean("permissions")) {
            
            return true;
            
        } else if (player.hasPermission("*") || player.hasPermission("gates.all") || player.hasPermission("gates.use.all")) {
            
            return true;
            
        } else if (player.hasPermission("gates.use.player.all") || player.hasPermission(String.format("gates.use.player.%s", this.owner.getName().toLowerCase()))) {
            
            return true;
            
        } else if (player.hasPermission("gates.use.type.all") || player.hasPermission(String.format("gates.use.type.%s", this.type.getName().toLowerCase()))) {
            
            return true;
            
        } else if ((player.equals(this.owner) && player.hasPermission("gates.use.self")) || (player.equals(this.owner) && player.hasPermission("gates.player")) || (!player.equals(this.owner) && player.hasPermission("gates.use.others"))) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }
    
    public boolean hasPermissionToModify(final Player player) {
    
        if (!this.plugin.getConfig().getBoolean("permissions")) {
            
            return true;
            
        } else if (player.hasPermission("*") || player.hasPermission("gates.all") || player.hasPermission("gates.modify.all")) {
            
            return true;
            
        } else if (player.hasPermission("gates.modify.player.all") || player.hasPermission(String.format("gates.modify.player.%s", this.owner.getName().toLowerCase()))) {
            
            return true;
            
        } else if (player.hasPermission("gates.modify.type.all") || player.hasPermission(String.format("gates.modify.type.%s", this.type.getName().toLowerCase()))) {
            
            return true;
            
        } else if ((player.equals(this.owner) && player.hasPermission("gates.modify.self")) || (player.equals(this.owner) && player.hasPermission("gates.player")) || (!player.equals(this.owner) && player.hasPermission("gates.modify.others"))) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }
    
    public boolean hasPermissionToDestroy(final Player player) {
    
        if (!this.plugin.getConfig().getBoolean("permissions")) {
            
            return true;
            
        } else if (player.hasPermission("*") || player.hasPermission("gates.all") || player.hasPermission("gates.destroy.all")) {
            
            return true;
            
        } else if (player.hasPermission("gates.destroy.player.all") || player.hasPermission(String.format("gates.destroy.player.%s", this.owner.getName().toLowerCase()))) {
            
            return true;
            
        } else if (player.hasPermission("gates.destroy.type.all") || player.hasPermission(String.format("gates.destroy.type.%s", this.type.getName().toLowerCase()))) {
            
            return true;
            
        } else if ((player.equals(this.owner) && player.hasPermission("gates.destroy.self")) || (player.equals(this.owner) && player.hasPermission("gates.player")) || (!player.equals(this.owner) && player.hasPermission("gates.destroy.others"))) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }
    
    public boolean isOpen() {
    
        for (final Block block : this.gateBlocks) {
            
            if (!Gates.empty.contains(block.getType())) { return false; }
            
        }
        
        return true;
        
    }
    
    public boolean isInstant() {
    
        return this.type.getDelay() == 0;
        
    }
    
    public void open() {
    
        if (!this.isInstant()) {
            
            if (this.solidBlocks.isEmpty()) { return; }
            
            Gates.busyGates.add(this.plugin.searchGate(this.solidBlocks.get(0)).getSign());
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, new GateTimer(true), this.type.getDelay());
            
        } else {
            
            for (final Block block : this.gateBlocks) {
                
                block.setType(Material.AIR);
                
            }
            
        }
        
    }
    
    public void close() {
    
        if (!this.isInstant()) {
            
            if (this.solidBlocks.isEmpty()) { return; }
            
            Gates.busyGates.add(this.plugin.searchGate(this.solidBlocks.get(0)).getSign());
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, new GateTimer(false), this.type.getDelay());
            
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
            
            for (final Block block : Gate.this.gateBlocks) {
                
                if (block.getType().equals(Gate.this.material) && Gates.empty.contains(block.getRelative(BlockFace.DOWN).getType())) {
                    
                    Gate.this.plugin.getServer().getScheduler().runTaskLater(Gate.this.plugin, new GateTimer(this.open), Gate.this.type.getDelay());
                    return;
                    
                }
                
            }
            
            if (Gate.this.solidBlocks.isEmpty()) { return; }
            
            Gates.busyGates.remove(Gate.this.plugin.searchGate(Gate.this.solidBlocks.get(0)).getSign());
            
        }
        
    }
    
}
