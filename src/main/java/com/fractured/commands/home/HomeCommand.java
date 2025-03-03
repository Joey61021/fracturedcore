
package com.fractured.commands.home;

import com.fractured.FracturedCore;
import com.fractured.events.home.Home;
import com.fractured.events.home.HomeManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public final class HomeCommand
{
    private HomeCommand()
    {
    }

    public static boolean home(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        // /home [name] to tp or /home for available homes
        Set<Home> homes = HomeManager.homes.get(player.getUniqueId());
        if (args.length == 0)
        {
            if (homes.isEmpty())
            {
                player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_HOME_NO_HOMES));
                return true;
            }

            StringBuilder sb = new StringBuilder();

            for (Home home : homes)
            {
                sb.append(home.getName()).append(" ");
            }

            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_HOME_AVAILABLE).replace("%homes%", sb.toString()));
            return true;
        }

        Home home = null;
        for (Home playerHomes : homes)
        {
            if (!playerHomes.getName().equalsIgnoreCase(args[0].toLowerCase()))
            {
                continue;
            }

            home = playerHomes;
            break;
        }

        if (home == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_HOME_NOT_FOUND));
            return true;
        }

        player.teleport(home.getLocation());
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_HOME_TELEPORTING).replace("%home%", home.getName()));
        return true;
    }
}
