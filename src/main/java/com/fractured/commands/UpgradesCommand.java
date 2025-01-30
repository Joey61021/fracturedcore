package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class UpgradesCommand
{
    private UpgradesCommand()
    {
    }

    public static boolean upgrades(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CONSOLE_BLOCKED));
            return true;
        }

        Player player = (Player) sender;
        Team team = UserManager.getUser(player).getTeam();

        if (team == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_TEAM_BLOCKED));
            return true;
        }

        player.openInventory(team.getUpgradesMenu());
        return true;
    }
}
