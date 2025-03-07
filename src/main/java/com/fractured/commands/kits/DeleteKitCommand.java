package com.fractured.commands.kits;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.kits.Kit;
import com.fractured.kits.KitManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class DeleteKitCommand
{

    private DeleteKitCommand()
    {
    }

    public static boolean deletekit(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!player.hasPermission(Permissions.COMMAND_CREATE_KIT))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        // /deletekit [name]
        if (args.length == 0)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_DELETEKIT_USAGE));
            return true;
        }

        Kit kit = KitManager.getKit(args[0]);
        if (kit == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_DELETEKIT_NOT_FOUND));
            return true;
        }

        Config kits = FracturedCore.getKits();

        kits.set("kits." + kit.getName().toLowerCase(), null);
        kits.save();

        KitManager.removeKit(kit);

        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_DELETEKIT_DELETED).replace("%kit%", kit.getName()));
        return true;
    }
}
