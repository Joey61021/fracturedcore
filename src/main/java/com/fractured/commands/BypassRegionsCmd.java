package com.fractured.commands;

import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BypassRegionsCmd implements CommandExecutor {

    public static ArrayList<Player> bypass = new ArrayList<>();

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, Message.CONSOLE_BLOCKED);
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("fractured.admin")) {
            MessageManager.sendMessage(player, Message.NO_PERMISSION);
            return false;
        }

        if (bypass.contains(player)) {
            bypass.remove(player);
            MessageManager.sendMessage(player, Message.CMD_BYPASS_REGIONS_TOGGLE_OFF);
            return false;
        }
        bypass.add(player);
        MessageManager.sendMessage(player, Message.CMD_BYPASS_REGIONS_TOGGLE_ON);
        return false;
    }
}
