package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Utils.Color(Message.CONSOLE_BLOCKED.getMessage()));
            return false;
        }

        Player player = (Player) sender;
        player.sendMessage(Utils.Color("&e" + player.getName() + "&f | &eMy Profile"));
        Teams team = TeamManager.getTeam(player);
        player.sendMessage(Utils.Color("&7- My Team: " + (team != null ? team.getColor() + team.getName() : "&7None")));
        return false;
    }
}
