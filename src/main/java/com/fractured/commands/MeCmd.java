package com.fractured.commands;

import com.fractured.enums.Teams;
import com.fractured.managers.TeamManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, Message.CONSOLE_BLOCKED);
            return false;
        }

        Player player = (Player) sender;
        player.sendMessage(Utils.color("&e" + player.getName() + "&f | &eMy Profile"));
        Teams team = TeamManager.getTeam(player);
        player.sendMessage(Utils.color("&7- My Team: " + (team != null ? team.getColor() + team.getName() + " team" : "&7None")));
        return false;
    }
}
