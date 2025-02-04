package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class BypassRegionsCommand
{
    private BypassRegionsCommand()
    {
    }

    public static boolean bypassRegions(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!player.hasPermission(Permissions.COMMAND_BYPASS_REGIONS))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        User user = UserManager.getUser(player.getUniqueId());
        if (user.getBypassRegions())
        {
            user.setBypassRegions(false);
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BYPASS_REGIONS_TOGGLE_OFF));
            return true;
        }
        user.setBypassRegions(true);
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BYPASS_REGIONS_TOGGLE_ON));
        return true;
    }
}
