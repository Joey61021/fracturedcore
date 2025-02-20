package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.events.WorldManager;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class EndCommand
{
    public static boolean end(final CommandSender sender, final Command command, String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        Team team = UserManager.getUser(player).getTeam();

        if (team == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_TEAM_BLOCKED));
            return true;
        }

        player.teleport(WorldManager.END_ROOM);
        sender.sendMessage(FracturedCore.getMessages().get(Messages.TELEPORTING));
        return true;
    }

    private EndCommand()
    {

    }
}
