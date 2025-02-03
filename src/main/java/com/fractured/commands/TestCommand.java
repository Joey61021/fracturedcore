package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.enchants.CustomEnchantment;
import com.fractured.enchants.EnchantmentManager;
import com.fractured.util.globals.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class TestCommand
{


    public static boolean test(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!sender.hasPermission("*"))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null)
        {
            meta.addEnchant(Enchantment.EFFICIENCY, 5, true);
            meta.addEnchant(Enchantment.UNBREAKING, 3, true);
            meta.addEnchant(Enchantment.FORTUNE, 3, true);
            meta.addEnchant(Enchantment.MENDING, 1, true);

            item.setItemMeta(meta);

            EnchantmentManager.enchant(item, CustomEnchantment.AUTO_SMELT, 1);
        }

        ((Player) sender).getInventory().addItem(item);
        return true;
    }

    private TestCommand()
    {

    }
}
