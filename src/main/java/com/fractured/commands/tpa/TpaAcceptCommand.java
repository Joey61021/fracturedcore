package com.fractured.commands.tpa;

import com.fractured.FracturedCore;
import com.fractured.events.tpa.TpaManager;
import com.fractured.events.tpa.TpaRequest;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpaAcceptCommand
{
    private TpaAcceptCommand()
    {
    }

    public static boolean accept(final CommandSender sender, final Command cmd, final String label, final String[] args)
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

        for (TpaRequest request : TpaManager.getRequests())
        {
            if (request.getRequested().equals(player.getUniqueId()))
            {
                TpaManager.getRequests().remove(request);

                Player target = Bukkit.getPlayer(request.getRequester());
                if (target == null)
                {
                    continue;
                }

                target.teleport(player);
                target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_ACCEPTED));
                return true;
            }
        }
        return true;
    }
}
