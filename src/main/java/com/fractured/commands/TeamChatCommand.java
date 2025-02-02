package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.team.TeamManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TeamChatCommand
{
    private TeamChatCommand()
    {
    }

    public static boolean teamchat(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        TeamManager.toggleTeamChat((Player) sender);
        return true;
    }
}
