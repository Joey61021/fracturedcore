package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RegionCheckCmd implements CommandExecutor {

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

        Teams team = LocationManager.getEnemyTeam(player.getLocation(), null);
        player.sendMessage(Utils.Color(Message.CMD_REGION_CHECK.getMessage().replace("%team%", team == null ? "&7None" : team.getColor() + team.getName())));
        return false;
    }
}
