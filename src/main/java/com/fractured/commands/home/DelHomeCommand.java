
package com.fractured.commands.home;

import com.fractured.FracturedCore;
import com.fractured.events.home.HomeManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class DelHomeCommand
{
    private DelHomeCommand()
    {
    }

    public static boolean delhome(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        // /sethome [name]
        if (args.length == 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_DELHOME_USAGE));
            return true;
        }

        String name = args[1].toLowerCase(); // Must be lowercase
        if (!HomeManager.exists(player, name))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_DELHOME_NOT_FOUND));
            return true;
        }

        HomeManager.delHome(player, name);
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_DELHOME_DELETED));
        return true;
    }
}
