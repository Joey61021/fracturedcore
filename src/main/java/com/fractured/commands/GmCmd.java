package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Utils.Color(Message.CONSOLE_BLOCKED.getMessage()));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("fractured.admin")) {
            player.sendMessage(Utils.Color(Message.NO_PERMISSION.getMessage()));
            return false;
        }

        if (player.getGameMode() == GameMode.SURVIVAL) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(Utils.Color(Message.CMD_GM_CREATIVE.getMessage()));
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(Utils.Color(Message.CMD_GM_SURVIVAL.getMessage()));
        }
        return false;
    }
}
