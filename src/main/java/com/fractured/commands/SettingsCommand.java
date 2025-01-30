package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.menu.MenuManager;
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

import java.util.ArrayList;
import java.util.List;

import static com.fractured.util.Utils.color;

public final class SettingsCommand
{
    // lol Java
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

        inventory = Bukkit.createInventory(null, 3 * 9, "Settings");

        // todo Perhaps make a config key which stores an itemstack or callback or something of the sort to modify the config?
        ItemStack item = new ItemStack(FracturedCore.getFracturedConfig().get(ConfigKeys.FRIENDLY_FIRE) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (meta != null)
        {
            meta.setDisplayName(ChatColor.GRAY + "Friendly Fire: " + ChatColor.GREEN + " On");
            lore.add(ConfigKeys.FRIENDLY_FIRE.getPath() + ": true");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        inventory.addItem(item);

        item = new ItemStack(FracturedCore.getFracturedConfig().get(ConfigKeys.STARTER_ITEMS) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);

        if (meta != null)
        {
            meta.setDisplayName(ChatColor.GRAY + "Starter Items: " + ChatColor.GREEN + " On");
            lore.set(0, ConfigKeys.FRIENDLY_FIRE.getPath() + ": true");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        inventory.addItem(item);
    }

    public static boolean settings(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_SETTINGS_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return true;
        }

        if (args.length == 0)
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_CONSOLE_USAGE));
                return true;
            }
            // todo this inventory still needs done
            ((Player) sender).openInventory(inventory);
        } else if (!settingsSubCommands.dispatch(sender, args))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_PLAYER_USAGE));
        }
        return true;
    }

    private static void settingsSet(final CommandSender sender, final String[] args)
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

    private static void settingsRefresh(final CommandSender sender, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_SETTINGS_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return;
        }

        sender.sendMessage("Refreshing...");
        FracturedCore.getFracturedConfig().refresh();
        sender.sendMessage("Refreshed.");
    }
}
