package com.fractured.commands.tpa;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class BackCommand
{
    private BackCommand()
    {
    }

    public static boolean back(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        User user = UserManager.getUser(player);
        if (user == null)
        {
            return true;
        }

        if (user.getLastLocation() == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BACK_NONE_SET));
            return true;
        }

        player.teleport(user.getLastLocation());
        user.setLastLocation(null);
        return true;
    }
}
