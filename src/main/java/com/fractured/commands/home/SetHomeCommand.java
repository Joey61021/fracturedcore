
package com.fractured.commands.home;

import com.fractured.FracturedCore;
import com.fractured.events.WorldManager;
import com.fractured.events.home.HomeManager;
import com.fractured.team.Claim;
import com.fractured.team.ClaimManager;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetHomeCommand
{
    private SetHomeCommand()
    {
    }

    public static boolean sethome(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        // /sethome [name]
        if (args.length == 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETHOME_USAGE));
            return true;
        }

        User user = UserManager.getUser(player);
        if (user == null)
        {
            return true;
        }

        Claim claim = ClaimManager.getClaim(player.getLocation());
        if (!player.getWorld().equals(WorldManager.OVER_WORLD) || claim == null || claim.getTeam() == null || claim.getTeam() != user.getTeam()) // must be in their team region and overworld
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETHOME_INVALID_LOCATION));
            return true;
        }

        if (HomeManager.homes.get(player.getUniqueId()).size() > HomeManager.MAX_HOMES)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETHOME_TOO_MANY));
            return true;
        }

        String name = args[0].toLowerCase(); // Needs to be lowercase
        if (HomeManager.exists(player, name))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETHOME_IN_USE));
            return true;
        }

        HomeManager.addHome(player, name);
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETHOME_SET).replace("%home%", name));
        return true;
    }
}
