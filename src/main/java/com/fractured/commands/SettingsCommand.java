package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.util.globals.ConfigKeys;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class SettingsCommand
{
    private SettingsCommand()
    {
    }

    private static final SubCommandRegistry settingsSubCommands;
    private static final Inventory inventory;

    static
    {
        settingsSubCommands = new SubCommandRegistry(0);
        settingsSubCommands.register("set", SettingsCommand::settingsSet, "s");
        settingsSubCommands.register("refresh", SettingsCommand::settingsRefresh, "reload", "rl", "r");

        inventory = Bukkit.createInventory(null, 9, "Settings");

        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();


        if (meta != null)
        {
            meta.setDisplayName(ChatColor.GRAY + "Friendly Fire: " + (FracturedCore.getFracturedConfig().get(ConfigKeys.FRIENDLY_FIRE) ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
            item.setItemMeta(meta);
        }

        inventory.setItem(1, item);

        item = new ItemStack(Material.WOODEN_PICKAXE);

        if (meta != null)
        {
            meta.setDisplayName(ChatColor.GRAY + "Starter Items: " +(FracturedCore.getFracturedConfig().get(ConfigKeys.STARTER_ITEMS) ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
            item.setItemMeta(meta);
        }

        inventory.setItem(7, item);
    }

    public static boolean settings(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_SETTINGS_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        if (args.length == 0)
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_CONSOLE_USAGE));
                return true;
            }
            ((Player) sender).openInventory(inventory);
        } else if (settingsSubCommands.dispatch(sender, cmd, label, args))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_PLAYER_USAGE));
        }
        return true;
    }

    private static void settingsSet(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /settings set [path] [value]
        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_SET));
            return;
        }

        if (!FracturedCore.getFracturedConfig().contains(args[1]))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_SET_INVALID_PATH));
            return;
        }

        FracturedCore.getFracturedConfig().set(args[1], args[2]);
        FracturedCore.getFracturedConfig().save();
        sender.sendMessage("Set " + args[1] + " to be " + args[2]);
    }

    private static void settingsRefresh(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_SETTINGS_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        sender.sendMessage("Refreshing...");
        FracturedCore.getFracturedConfig().refresh();
        sender.sendMessage("Refreshed.");
    }
}
