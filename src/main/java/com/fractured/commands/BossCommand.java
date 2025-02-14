package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.events.BossManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class BossCommand
{
    private BossCommand()
    {
    }

    public static boolean boss(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!player.hasPermission(Permissions.COMMAND_BOSS))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        BossManager.spawnBoss(player.getLocation());
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BOSS_SPAWNED));
        return true;
    }
}
