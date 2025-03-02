package com.fractured.commands.messages;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ReplyCommand
{
    private ReplyCommand()
    {
    }

    public static boolean reply(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (args.length == 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_REPLY_USAGE));
            return true;
        }

        User user = UserManager.getUser(player);
        if (user == null)
        {
            return true;
        }

        if (user.getLastMessage() == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_REPLY_NO_RECENT));
            return true;
        }

        Player target = Bukkit.getPlayer(user.getLastMessage());
        if (target == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_REPLY_NOT_ONLINE));
            user.setLastMessage(null);
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++)
        {
            sb.append(args[i]).append(" ");
        }
        String message = sb.toString();
        Utils.sendPlayerMessage(player, target, message);
        return true;
    }
}
