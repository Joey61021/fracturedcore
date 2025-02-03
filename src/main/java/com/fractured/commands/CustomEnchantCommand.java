package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.enchants.CustomEnchantment;
import com.fractured.enchants.EnchantmentManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CustomEnchantCommand
{
    public static boolean customEnchant(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!sender.hasPermission(Permissions.COMMAND_CUSTOM_ENCHANT))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        // /cenchant [enchantment] [level]
        if (args.length < 2)
        {
            //todo
            sender.sendMessage("Usage: /customenchant [enchantment] [level]");
            return true;
        }

        CustomEnchantment enchant;

        try
        {
            enchant = CustomEnchantment.getByName(args[0].toLowerCase());
        } catch (IllegalArgumentException e)
        {
            sender.sendMessage("Unknown enchantment: " + args[0]);
            return true;
        }

        int level;

        try
        {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e)
        {
            // todo
            sender.sendMessage("Level must be an integer");
            return true;
        }

        switch (EnchantmentManager.enchant(((Player) sender).getInventory().getItemInMainHand(), enchant, level))
        {
            case 0:
                sender.sendMessage("Enchanted.");
                break;
            case 1:
                sender.sendMessage("Too high of a level: (Must be below " + enchant.maxLevel() + ")");
                break;
            case 2:
                sender.sendMessage("Not assignable to this item.");
                break;
        }

        return true;
    }

    private CustomEnchantCommand()
    {
    }
}
