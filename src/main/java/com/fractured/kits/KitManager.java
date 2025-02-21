package com.fractured.kits;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

public class KitManager implements Listener
{

    public static Set<Kit> kits = new HashSet<>();

    public static void addKit(String name, int cooldown, Set<KitItem> items)
    {
        kits.add(new Kit(kits.size(), name, cooldown, items));
    }

    public static Kit getKit(String name)
    {
        for (Kit kit : kits)
        {
            if (kit.getName().equalsIgnoreCase(name))
            {
                return kit;
            }
        }

        return null; // No kit found
    }

    public static void applyKit(Player player, Kit kit)
    {
        Inventory inventory = player.getInventory();
        for (KitItem item : kit.getItems())
        {
            inventory.addItem(new ItemStack(item.getMaterial(), item.getAmount()));
        }

        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_KIT_RECEIVED).replace("%kit%", kit.getName()));
    }

    public static void showGUI(Player player)
    {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Kits");

        for (Kit kit : kits)
        {
            ItemStack item = new ItemStack(kit.getMaterial());
            ItemMeta meta = item.getItemMeta();

            if (meta != null)
            {
                meta.setDisplayName(kit.getColor() + (kit.getName().toUpperCase().charAt(0) + kit.getName().substring(1)));
                item.setItemMeta(meta);
            }

            inventory.addItem(item);
        }

        player.openInventory(inventory);
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_KITS_OPENING));
    }

    @EventHandler
    public static void onClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();

        if (player.getInventory() == event.getClickedInventory() || !event.getView().getTitle().equalsIgnoreCase("Kits"))
        {
            return;
        }

        event.setCancelled(true);
    }
}
