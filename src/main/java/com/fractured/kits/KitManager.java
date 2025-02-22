package com.fractured.kits;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KitManager implements Listener
{

    public static Set<Kit> activeKits = new HashSet<>();
    public static Set<KitCooldown> cooldowns = new HashSet<>();

    public static void initKits()
    {
        activeKits.clear(); // Just in case this gets run twice

        try
        {
            Config kits = FracturedCore.getKits();
            for (String kit : kits.getConfigurationSection("kits").getKeys(false))
            {

                System.out.println(kit); // debug shit

                Set<KitItem> kitItems = new HashSet<>();

                for (String item : kits.getConfigurationSection("kits." + kit + ".items").getKeys(false))
                {
                    String path = "kits." + kit + ".items." + item;
                    try
                    {
                        KitItem kitItem = new KitItem(Material.valueOf(kits.getString(path + ".material").toUpperCase()), kits.getInt(path + ".amount"));

                        if (kits.isSet(path + "name"))
                        {
                            kitItem.setName(kits.getString(path + ".name"));
                        }

                        kitItems.add(kitItem);
                    } catch (Exception ignored)
                    {
                        Bukkit.getLogger().warning("Failed to add item from path: " + path);
                    }
                }

                activeKits.add(new Kit(activeKits.size(), kit, 10, kitItems));
            }
        } catch (Exception ignored)
        {
            Bukkit.getLogger().warning("An issue has occurred loading kits.");
        }
    }

    public static void addKit(String name, int cooldown, Set<KitItem> items)
    {
        activeKits.add(new Kit(activeKits.size(), name, cooldown, items));
    }

    public static void removeKit(Kit kit)
    {
        activeKits.remove(kit);
    }

    public static Kit getKit(String name)
    {
        for (Kit kit : activeKits)
        {
            if (kit.getName().equalsIgnoreCase(name))
            {
                return kit;
            }
        }

        return null; // No kit found
    }

    public static Kit getKit(int id)
    {
        for (Kit kit : activeKits)
        {
            if (kit.getId() == id)
            {
                return kit;
            }
        }

        return null; // No kit found
    }

    public static KitCooldown getCooldown(Player player, Kit kit)
    {
        KitCooldown cooldown = null;
        for (KitCooldown cooldowns : cooldowns)
        {
            if (cooldowns.getUuid().equals(player.getUniqueId()) && cooldowns.getKitId() == kit.getId())
            {
                cooldown = cooldowns;
            }
        }

        return cooldown;
    }

    public static Set<Kit> getKitsOnCooldown(Player player)
    {
        Set<Kit> kits = new HashSet<>();
        for (KitCooldown cooldowns : cooldowns)
        {
            if (cooldowns.getUuid().equals(player.getUniqueId()))
            {
                kits.add(getKit(cooldowns.getKitId()));
            }
        }

        return kits;
    }

    public static void applyKit(Player player, Kit kit)
    {
        KitCooldown cooldown = getCooldown(player, kit);
        if (cooldown != null)
        {
            long timeLapsed = System.currentTimeMillis() - cooldown.getTimestamp();
            long cooldownTime = kit.getCooldown() * 1000L;
            if (timeLapsed < cooldownTime)
            {
                player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_KIT_ON_COOLDOWN).replace("%time%", String.valueOf(Math.round((float) (cooldownTime - timeLapsed) / 1000L))));
                return;
            }

            cooldowns.remove(cooldown);
        }

        Inventory inventory = player.getInventory();
        for (KitItem item : kit.getItems())
        {
            inventory.addItem(new ItemStack(item.getMaterial(), item.getAmount()));
        }

        cooldowns.add(new KitCooldown(player.getUniqueId(), kit.getId(), kit.getCooldown()));
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_KIT_RECEIVED).replace("%kit%", kit.getName()));
    }

    public static void showGUI(Player player)
    {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Kits");

        for (Kit kit : activeKits)
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
