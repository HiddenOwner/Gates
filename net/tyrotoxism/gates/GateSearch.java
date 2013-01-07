package net.tyrotoxism.gates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class GateSearch {
    
    private final Gates plugin;
    private final Block orginalBlock;
    private Material material = null;
    private final List<Block> blocks = new ArrayList<Block>();
    
    private Gate gate;
    
    public GateSearch(final Gates plugin, final Block orginalBlock) {
    
        this.plugin = plugin;
        this.orginalBlock = orginalBlock;
        
        this.searchGate();
        
    }
    
    public Block getOrginalBlock() {
    
        return this.orginalBlock;
        
    }
    
    public Material getMaterial() {
    
        return this.material;
        
    }
    
    public List<Block> getBlocks() {
    
        return this.blocks;
        
    }
    
    public Gate getGate() {
    
        return this.gate;
        
    }
    
    private void searchGate() {
    
        Block blockA = this.orginalBlock.getRelative(BlockFace.DOWN);
        
        while (Gates.blocks.contains((blockA = blockA.getRelative(BlockFace.UP)).getType()) || Gates.empty.contains(blockA.getType())) {
            
            if (Gates.blocks.contains(blockA.getType())) {
                
                this.material = blockA.getType();
                this.searchGateBlocks(blockA, 1);
                
            }
            
        }
        
        final int radius = this.plugin.getConfig().getInt("search-radius");
        
        for (final Block blockB : this.blocks) {
            
            for (int x = blockB.getX() - radius; x <= (blockB.getX() + radius); x++) {
                
                for (int y = blockB.getY() - radius; y <= (blockB.getY() + radius); y++) {
                    
                    for (int z = blockB.getZ() - radius; z <= (blockB.getZ() + radius); z++) {
                        
                        this.gate = this.plugin.getGate(blockB.getWorld().getBlockAt(x, y, z));
                        
                        if ((this.gate != null) && (this.gate.getGateBlocks().contains(blockB) || this.gate.getSolidBlocks().contains(blockB))) { return; }
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
    private void searchGateBlocks(final Block block, final int radius) {
    
        for (int x = block.getX() - radius; x <= (block.getX() + radius); x++) {
            
            for (int y = block.getY() - radius; y <= (block.getY() + radius); y++) {
                
                for (int z = block.getZ() - radius; z <= (block.getZ() + radius); z++) {
                    
                    final Block blockA = block.getWorld().getBlockAt(x, y, z);
                    
                    if (this.blocks.contains(blockA)) {
                        
                        continue;
                        
                    }
                    
                    if (this.material.equals(blockA.getType())) {
                        
                        this.blocks.add(blockA);
                        this.searchGateBlocks(blockA, 1);
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
}
