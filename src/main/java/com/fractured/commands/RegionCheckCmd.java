package com.fractured.commands;

import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegionCheckCmd implements CommandExecutor {

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

        Teams team = LocationManager.getEnemyTeam(player.getLocation(), null);
        MessageManager.sendMessage(player, Message.CMD_REGION_CHECK, (s) -> s.replace("%team%", team == null ? "&7None" : team.getColor() + team.getName()));
        return false;
    }
}
