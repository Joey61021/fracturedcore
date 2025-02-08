package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpawnCommand
{
    public static boolean spawn(final CommandSender sender, final Command command, String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        User user = UserManager.getUser((Player) sender);

        if (user.getTeam() == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CMD_SPAWN_NOT_IN_REGION));
            return true;
        }

        ((Player) sender).teleport(user.getTeam().spawn());
        sender.sendMessage(FracturedCore.getMessages().get(Messages.CMD_SPAWN_TELEPORTED));
        return true;
    }

    private SpawnCommand()
    {

    }
}
