package net.tyrotoxism.gates;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class Gate {
    
    // private final List<Block> blocks = new ArrayList<Block>();
    
    private final Gates plugin;
    
    private final Sign sign;
    private final OfflinePlayer owner;
    private final GateType type;
    private final GateRedstone redstone;
    
    private boolean open;
    private boolean ready;
    
    public Gate(final Gates plugin, final Sign sign) {
    
        this.plugin = plugin;
        
        this.sign = sign;
        this.owner = sign.getLine(1).isEmpty() ? null : this.plugin.getServer().getOfflinePlayer(sign.getLine(1));
        this.type = sign.getLine(2).isEmpty() ? null : this.plugin.getType(sign.getLine(2));
        this.redstone = sign.getLine(3).isEmpty() ? null : GateRedstone.valueOf(sign.getLine(3));
        
        this.open = true;
        this.ready = true;
        
        // Find the blocks and check if they're used by another gate
        
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
    
        return this.open;
        
    }
    
    public boolean isReady() {
    
        return this.ready;
        
    }
    
    public void open(final Player player) {
    
        if (!((player == null) || this.hasPermissionToUse(player))) { return; }
        
        this.open = true;
        this.ready = false;
        
        // Open the gate
        
    }
    
    public void close(final Player player) {
    
        if (!((player == null) || this.hasPermissionToUse(player))) { return; }
        
        this.open = false;
        this.ready = false;
        
        // Close the gate
        
    }
    
}
