package com.fractured.commands.kits;

import com.fractured.FracturedCore;
import com.fractured.kits.Kit;
import com.fractured.kits.KitManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class KitCommand
{

    private KitCommand()
    {
    }

    public static boolean kit(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (args.length == 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_KIT_USAGE));
            return true;
        }

        Kit kit = KitManager.getKit(args[0]);
        if (kit == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_KIT_NOT_FOUND));
            return true;
        }

        KitManager.applyKit(player, kit);
        return true;
    }
}
