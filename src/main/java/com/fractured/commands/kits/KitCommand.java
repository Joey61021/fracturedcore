package com.fractured.commands.kits;

import com.fractured.FracturedCore;
import com.fractured.kits.Kit;
import com.fractured.kits.KitManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

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
            StringBuilder stringBuilder = new StringBuilder();
            Set<Kit> kitsOnCooldown = KitManager.getKitsOnCooldown(player);

            for (Kit kits : KitManager.activeKits)
            {
                stringBuilder.append(Utils.color((kitsOnCooldown.contains(kits) ? "&m" : "&r") + kits.getName() + "&r")).append(" ");
            }

            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_KIT_AVAILABLE).replace("%kits%", stringBuilder.toString()));
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
