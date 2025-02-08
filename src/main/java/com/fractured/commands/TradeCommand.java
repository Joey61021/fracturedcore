package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.trades.TradeManager;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TradeCommand
{
    private TradeCommand()
    {
    }

    public static boolean trade(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (args.length == 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TRADE_USAGE));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TARGET));
            return true;
        }

        TradeManager.startTrade(player, target);
        return true;
    }
}
