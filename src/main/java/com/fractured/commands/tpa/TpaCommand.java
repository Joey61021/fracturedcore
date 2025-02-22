package com.fractured.commands.tpa;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class TpaCommand
{
    private TpaCommand()
    {
    }

    private static Set<TpaRequest> requests = new HashSet<>();
    private static final long COOLDOWN = 60000;

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

        // User is accepting a tp request
        for (TpaRequest request : requests)
        {
            if (request.getRequester().equals(target.getUniqueId()))
            {
                target.teleport(player);
                target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_ACCEPTED));
                requests.remove(request);
                return true;
            }
        }

        // Request already sent
        for (TpaRequest request : requests)
        {
            if (request.getRequester().equals(player.getUniqueId()))
            {
                long lapsed = System.currentTimeMillis() - request.getTimestamp();
                if (lapsed >= COOLDOWN)
                {
                    requests.remove(request);
                    continue; // Ignore if cooldown has lapsed
                }

                player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_ALREADY_SENT).replace("%time%", String.valueOf((COOLDOWN - lapsed) / 1000)));
                return true;
            }
        }

        requests.add(new TpaRequest(player.getUniqueId(), target.getUniqueId()));
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_SENT).replace("%receiver%", target.getName()));
        target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_RECEIVED).replace("%sender%", player.getDisplayName()));
        return true;
    }
}
