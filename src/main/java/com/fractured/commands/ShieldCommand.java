package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.menu.ItemBuilder;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ShieldCommand
{
    private ShieldCommand()
    {
    }

    public static void giveShield(Player player)
    {
        ItemBuilder item = new ItemBuilder(Material.BEACON);
        item.name("&bProtection Shield");
        item.lore("&7Protects land even when offline up to a certain distance.");
        player.getInventory().addItem(item.build());
    }

    public static boolean shield(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!player.hasPermission(Permissions.COMMAND_SHIELD_ADMIN))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        giveShield(player);
        return true;
    }
}
