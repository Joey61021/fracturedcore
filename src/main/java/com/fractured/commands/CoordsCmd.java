package com.fractured.commands;

import com.fractured.util.globals.Messages;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.CONSOLE_BLOCKED);
            return true;
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        player.chat("My coords are: X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ() + "!");
        return true;
    }
}
