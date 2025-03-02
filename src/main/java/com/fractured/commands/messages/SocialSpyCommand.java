package com.fractured.commands.messages;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SocialSpyCommand
{
    private SocialSpyCommand()
    {
    }

    public static boolean socialSpy(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        if (!(player.hasPermission(Permissions.COMMAND_SOCIAL_SPY)))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        Player target = player;
        if (args.length > 0)
        {
            target = Bukkit.getPlayer(args[0]);
        }

        if (target == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.NO_PLAYER));
            return true;
        }

        User user = UserManager.getUser(target);
        if (user == null)
        {
            return true;
        }

        if (user.getSocialSpy())
        {
            user.setSocialSpy(false);
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SOCIAL_SPY_TOGGLE_OFF).replace("%player%", target.getName()));
            return true;
        }

        user.setSocialSpy(true);
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SOCIAL_SPY_TOGGLE_ON).replace("%player%", target.getName()));
        return true;
    }
}
