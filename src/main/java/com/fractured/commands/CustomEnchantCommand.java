package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.enchants.Enchant;
import com.fractured.enchants.EnchantManager;
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
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CUSTOM_ENCHANT_USAGE));
            return true;
        }

        Enchant enchant;

        try
        {
            enchant = Enchant.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CUSTOM_ENCHANT_UNKNOWN));
            return true;
        }

        int level;

        try
        {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.MUST_BE_INT));
            return true;
        }

        switch (EnchantManager.enchant(((Player) sender).getInventory().getItemInMainHand(), enchant, level))
        {
            case 0:
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CUSTOM_ENCHANT_APPLIED).replace("%enchant%", enchant.display()));
                break;
            case 1:
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CUSTOM_ENCHANT_LEVEL_BOUNDS).replace("%level%", String.valueOf(enchant.maxLevel())));
                break;
            case 2:
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CUSTOM_ENCHANT_BAD_ITEM_TYPE));
                break;
        }

        return true;
    }

    private CustomEnchantCommand()
    {
    }
}
