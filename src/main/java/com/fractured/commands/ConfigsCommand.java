package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.util.globals.ConfigKeys;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ConfigsCommand
{
    private ConfigsCommand()
    {
    }

    private static final SubCommandRegistry configsSubCommands;
    private static final Inventory inventory;

    static
    {
        configsSubCommands = new SubCommandRegistry(0);
        configsSubCommands.register("set", ConfigsCommand::configsSet, "s");
        configsSubCommands.register("refresh", ConfigsCommand::configsRefresh, "reload", "rl", "r");

//        MenuBuilder builder = new MenuBuilder();
//
//        for (Field field : ConfigKeys.class.getDeclaredFields())
//        {
//            if (ConfigKey.class.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers()))
//            {
//
//            }
//        }

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

    public static boolean configs(final CommandSender sender, final Command cmd, final String label, final String[] args)
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
            // fixme console
            ((Player) sender).openInventory(inventory);
        } else if (configsSubCommands.dispatch(sender, cmd, label, args))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_PLAYER_USAGE));
        }
        return true;
    }

    private static void configsSet(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SETTINGS_SET));
            return;
        }

        if (args.length < 3)
        {
            // /configs set [config] [path] [value]

        } else
        {
            // /configs set [path] [value]

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

    private static void configsRefresh(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /configs refresh (config)

        if (!sender.hasPermission(Permissions.COMMAND_SETTINGS_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        if (args.length < 2)
        {
            // reload config
            sender.sendMessage("Are you sure you want to refresh the config? It may cause immense lag to the server. Type /confirm to proceed.");
        } else
        {
            // reload specified
//            if (args)

        }

        ConfirmationManager.addConfirmation(sender, verifier ->
        {
            verifier.sendMessage("Refreshing...");
            FracturedCore.getFracturedConfig().refresh();
            verifier.sendMessage("Done.");
        });
    }
}
