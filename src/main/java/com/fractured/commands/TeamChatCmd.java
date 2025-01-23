package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.team.TeamManager;
import com.fractured.user.User;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChatCmd implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Messages.CONSOLE_BLOCKED);
            return true;
        }

        Player player = (Player) sender;
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());

        TeamManager.toggleTeamChat(player, user);
        return true;
    }
}
