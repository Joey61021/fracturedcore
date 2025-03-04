package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class EnchantCommand
{
    private EnchantCommand()
    {
    }

    public static boolean enchant(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!player.hasPermission(Permissions.COMMAND_ENCHANT))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        // /enchant [enchantment] [level]
        if (args.length < 2)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_ENCHANT_USAGE));
            return true;
        }

        Enchantment enchant = Enchantment.getByName(args[0]);
        if (enchant == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_ENCHANT_INVALID_ENCHANT));
            return true;
        }

        int level;
        try
        {
            level = Integer.parseInt(args[1]);
        } catch (Exception ignored)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_ENCHANT_USAGE));
            return true;
        }

        if (level < 0 || level > 255)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_ENCHANT_INVALID_RANGE));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_ENCHANT_NO_ITEM));
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchant, level, true);
        item.setItemMeta(meta);

        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_ENCHANT_ENCHANTED).replace("%enchant%", enchant.getName()));
        return true;
    }
}
