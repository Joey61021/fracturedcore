package com.fractured.commands.messages;

import com.fractured.FracturedCore;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MessageCommand
{
    private MessageCommand()
    {
    }

    public static boolean message(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (args.length < 2)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_MESSAGE_USAGE));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.NO_PLAYER));
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId()))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_MESSAGE_SELF));
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++)
        {
            sb.append(args[i]).append(" ");
        }
        Utils.sendPlayerMessage(player, target, sb.toString());
        return true;
    }
}
