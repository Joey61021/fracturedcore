package com.fractured.commands;

import com.fractured.enums.Teams;
import com.fractured.managers.SettingsManager;
import com.fractured.managers.TeamManager;
import com.fractured.managers.UpgradesManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpgradesCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, Message.CONSOLE_BLOCKED);
            return false;
        }

        Player player = (Player) sender;
        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            MessageManager.sendMessage(player, Message.NO_TEAM);
            return false;
        }

        UpgradesManager.displayGUI(player, team);
        return false;
    }
}
