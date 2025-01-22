package com.fractured.commands;

import com.fractured.managers.TeamManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChatCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, Message.CONSOLE_BLOCKED);
            return false;
        }

        Player player = (Player) sender;

        if (TeamManager.getTeam(player) == null) {
            MessageManager.sendMessage(player, Message.CMD_TC_NOT_IN_TEAM);
            return false;
        }

        if (TeamManager.toggleTeamChat(player)) {
            MessageManager.sendMessage(player, Message.CMD_TC_TOGGLE_ON);
            return false;
        }

        MessageManager.sendMessage(player, Message.CMD_TC_TOGGLE_OFF);
        return false;
    }
}
