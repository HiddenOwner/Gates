package com.geekzmultimedia.gates;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GateCommand implements CommandExecutor {
    
    private final Gates plugin;
    
    public GateCommand(final Gates plugin) {
    
        this.plugin = plugin;
        
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    
        if (!(sender instanceof Player)) {
            
            sender.sendMessage(String.format("%s%s", ChatColor.RED, "I'm sorry, but this command is only for in-game usage."));
            return true;
            
        }
        
        final Player player = (Player) sender;
        final Gate gate = this.plugin.getGate(player.getTargetBlock(null, 16));
        
        if (gate == null) {
            
            player.sendMessage("§cNot a valid gate sign, command not executed.");
            return true;
            
        }
        
        if (!gate.hasPermissionToModify(player)) {
            
            player.sendMessage("§cYou don't have permission to modify this gate sign.");
            return true;
            
        }
        
        try {
            
            if (args[0].equals("owner")) {
                
                gate.getSign().setLine(1, args[1]);
                gate.getSign().update();
                player.sendMessage("§eGate owner has been changed.");
                
            } else if (args[0].equals("type")) {
                
                gate.getSign().setLine(2, args[1]);
                gate.getSign().update();
                player.sendMessage("§eGate type has been changed.");
                
            } else if (args[0].equals("redstone")) {
                
                final GateRedstone redstone;
                
                try {
                    
                    redstone = GateRedstone.valueOf(args[1].toUpperCase());
                    
                } catch (final IllegalArgumentException e1) {
                    
                    player.sendMessage(String.format("§c%s is not a valid redstone state for this gate sign.", args[1].toUpperCase()));
                    return true;
                    
                }
                
                gate.getSign().setLine(3, redstone.name());
                gate.getSign().update();
                player.sendMessage("§eGate redstone state has been changed.");
                
            } else {
                
                return false;
                
            }
            
        } catch (final NullPointerException e) {
            
            return false;
            
        }
        
        return true;
        
    }
    
}
