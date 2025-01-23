package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.util.globals.Messages;
import com.fractured.team.Team;
import com.fractured.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCmd implements CommandExecutor
{

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            System.out.println(Utils.color(Messages.CONSOLE_BLOCKED));
            return false;
        }

        Player player = (Player) sender;
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Utils.color("&e" + player.getName() + "&f | &eMy Profile"));
        Team team = user.getTeam();
        player.sendMessage(Utils.color("&7- My Team: " + (team != null ? team.color() + team.getName() : "&7None")));
        return false;
    }
}
