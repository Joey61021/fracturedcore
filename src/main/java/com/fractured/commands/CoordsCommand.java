package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CoordsCommand
{
    private CoordsCommand()
    {
    }

    public static boolean coords(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        Location loc = player.getLocation();
        player.chat("My coords are: X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ() + "!");
        return true;
    }
}
