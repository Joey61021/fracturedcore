package com.fractured.commands.tpa;

import com.fractured.FracturedCore;
import com.fractured.events.tpa.TpaManager;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpaGenericCommand
{
    private TpaGenericCommand()
    {
    }

    public static boolean tpa(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (args.length == 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_USAGE));
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
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_TPA_SELF));
            return true;
        }

        int cooldown = TpaManager.getCooldown(player);
        if (TpaManager.getCooldown(player) > 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_ALREADY_SENT).replace("%time%", String.valueOf(cooldown)));
            return true;
        }

        TpaManager.addRequest(!cmd.getName().equalsIgnoreCase("tpahere"), player, target);
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_SENT).replace("%receiver%", target.getName()));
        target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_RECEIVED).replace("%sender%", player.getDisplayName()));
        return true;
    }
}
