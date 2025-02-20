package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpawnCommand
{
    public static boolean spawn(final CommandSender sender, final Command command, String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        Team team = UserManager.getUser(player).getTeam();

        if (team == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.CMD_SPAWN_NOT_IN_REGION));
            return true;
        }

        player.teleport(team.spawn());
        sender.sendMessage(FracturedCore.getMessages().get(Messages.CMD_SPAWN_TELEPORTED).replace("%team%", team.color() + team.getName()));
        return true;
    }

    private SpawnCommand()
    {

    }
}
