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
    
    private final String permission;
    
    private boolean open;
    private boolean ready;
    
    public Gate(final Gates plugin, final Sign sign) {
    
        this.plugin = plugin;
        
        this.sign = sign;
        this.owner = sign.getLine(1).isEmpty() ? null : this.plugin.getServer().getOfflinePlayer(sign.getLine(1));
        this.type = sign.getLine(2).isEmpty() ? null : this.plugin.getType(sign.getLine(2));
        this.redstone = sign.getLine(3).isEmpty() ? null : GateRedstone.valueOf(sign.getLine(3));
        
        this.permission = "sup";
        
        this.ready = true;
        this.open = true;
        
        // Find the blocks and check if they're used by another gate
        
    }
    
    public Sign getSign() {
    
        return this.sign;
        
    }
    
    public OfflinePlayer getOwner(final OfflinePlayer owner) {
    
        return (this.owner == null) ? owner : this.owner;
        
    }
    
    public GateType getType(final GateType type) {
    
        return (this.type == null) ? type : this.type;
        
    }
    
    public GateRedstone getRedstone(final GateRedstone redstone) {
    
        return (this.redstone == null) ? redstone : this.redstone;
        
    }
    
    public String getPermission() {
    
        return this.permission;
        
    }
    
    public boolean hasPermission(final Player player) {
    
        return player.hasPermission(this.permission) || player.hasPermission(this.type.getPermission());
        
    }
    
    public boolean isOpen() {
    
        return this.open;
        
    }
    
    public boolean isReady() {
    
        return this.ready;
        
    }
    
    public void open(final Player player) {
    
        if (!((player == null) || this.hasPermission(player))) { return; }
        
        this.open = true;
        this.ready = false;
        
        // Open the gate
        
    }
    
    public void close(final Player player) {
    
        if (!((player == null) || this.hasPermission(player))) { return; }
        
        this.open = false;
        this.ready = false;
        
        // Close the gate
        
    }
    
}
