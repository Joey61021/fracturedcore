package com.fractured.commands;

import com.fractured.managers.TeamManager;
import com.fractured.enums.Message;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChatCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Utils.Color(Message.CONSOLE_BLOCKED.getMessage()));
            return false;
        }

        Player player = (Player) sender;

        if (TeamManager.getTeam(player) == null) {
            player.sendMessage(Utils.Color(Message.CMD_TC_NOT_IN_TEAM.getMessage()));
            return false;
        }

        if (TeamManager.toggleTeamChat(player)) {
            player.sendMessage(Utils.Color(Message.CMD_TC_TOGGLE_ON.getMessage()));
            return false;
        }

        player.sendMessage(Utils.Color(Message.CMD_TC_TOGGLE_OFF.getMessage()));
        return false;
    }
}
