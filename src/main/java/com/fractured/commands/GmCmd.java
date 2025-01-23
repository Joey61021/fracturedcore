package com.fractured.commands;

import com.fractured.util.globals.Messages;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Messages.CONSOLE_BLOCKED);
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("fractured.admin")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return false;
        }

        if (player.getGameMode() == GameMode.SURVIVAL) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(Messages.CMD_GM_CREATIVE);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(Messages.CMD_GM_SURVIVAL);
        }
        return false;
    }
}
