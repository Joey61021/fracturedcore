package com.fractured.commands.kits;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.kits.KitItem;
import com.fractured.kits.KitManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public final class CreateKitCommand
{

    private CreateKitCommand()
    {
    }

    public static boolean createkit(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!player.hasPermission(Permissions.COMMAND_CREATEKIT))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        // /createkit [cooldown] [name]
        if (args.length < 2)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CREATEKIT_USAGE));
            return true;
        }

        int cooldown;
        try
        {
            cooldown = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.MUST_BE_INT));
            return true;
        }

        Config kits = FracturedCore.getKits();
        Inventory inv = player.getInventory();
        String name = args[1].toLowerCase();

        kits.set(name + ".cooldown", cooldown);
        Set<KitItem> kitItems = new HashSet<>();

        int i = 0; // Increment only if it's a valid item
        for (ItemStack item : inv.getContents())
        {
            if (item == null || item.getType() == Material.AIR)
            {
                continue;
            }

            kits.set("kits." + name + "." + i + ".material", item.getType().toString().toUpperCase()); // e.g. STONE_SWORD
            kits.set("kits." + name + "." + i + ".amount", item.getAmount());

            kitItems.add(new KitItem(item.getType(), item.getAmount()));

            i++;
        }

        kits.save();
        KitManager.addKit(name, cooldown, kitItems);
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CREATEKIT_SAVED).replace("%name%", name).replace("%cooldown%", String.valueOf(cooldown)));
        return true;
    }
}
