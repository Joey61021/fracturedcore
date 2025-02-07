package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetMaxHealthCommand
{
    public static boolean setMaxHealth(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_SET_MAX_HEALTH))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        if (args.length == 0)
        {
            sender.sendMessage("Usage: /setmaxhealth (target) [health]");
            return true;
        }

        if (args.length == 1)
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
                return true;
            }

            try
            {
                double health = Double.parseDouble(args[0]);

                sender.sendMessage("Updated.");
                ((Player) sender).setMaxHealth(health);
            } catch (NumberFormatException e)
            {
                sender.sendMessage("Invalid health");
                return true;
            }
        } else
        {
            Player player = Bukkit.getPlayer(args[0]);

            if (player == null)
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TARGET));
                return true;
            }

            try
            {
                double health = Double.parseDouble(args[0]);

                sender.sendMessage("Updated.");
                player.setMaxHealth(health);
            } catch (NumberFormatException e)
            {
                sender.sendMessage("Invalid health");
                return true;
            }
        }
        return true;
    }

    private SetMaxHealthCommand()
    {
    }
}
