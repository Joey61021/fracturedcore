package com.fractured.cevents;

import com.fractured.FracturedCore;
import com.fractured.cevents.loot.LootItem;
import com.fractured.events.WorldManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EventManager {

    // todo: time
    public static Set<CEvent> events = new HashSet<>();
    public static List<LootItem> crateLoot = new ArrayList<>();
    public static CEvent runningEvent = null;

    public static CEvent getRunningEvent()
    {
        return runningEvent;
    }

    public static void init()
    {
        registerCEvent(new CEvent(ChatColor.YELLOW, "Crates", 10));

        // todo: config/loot yml for loot
        crateLoot.add(new LootItem(Material.WOODEN_SWORD, 1, 2));
        crateLoot.add(new LootItem(Material.WOODEN_AXE, 1, 2));
        crateLoot.add(new LootItem(Material.WOODEN_PICKAXE, 1, 2));
        crateLoot.add(new LootItem(Material.STONE_SWORD, 1, 2));
        crateLoot.add(new LootItem(Material.STONE_AXE, 1, 2));
        crateLoot.add(new LootItem(Material.STONE_PICKAXE, 1, 2));
        crateLoot.add(new LootItem(Material.GOLDEN_SWORD, 1, 2));
        crateLoot.add(new LootItem(Material.GOLDEN_AXE, 1, 2));
        crateLoot.add(new LootItem(Material.GOLDEN_PICKAXE, 1, 2));
        crateLoot.add(new LootItem(Material.DIAMOND_SWORD, 1, 2));
        crateLoot.add(new LootItem(Material.DIAMOND_AXE, 1, 2));
        crateLoot.add(new LootItem(Material.DIAMOND_PICKAXE, 1, 2));
        crateLoot.add(new LootItem(Material.GOLDEN_APPLE, 6, 10));
        crateLoot.add(new LootItem(Material.COOKED_BEEF, 16, 10));
        crateLoot.add(new LootItem(Material.OAK_WOOD, 16, 10));
    }

    public static void registerCEvent(CEvent cEvent)
    {
        events.add(cEvent);
    }

    public static void runEvent(CEvent event)
    {
        runningEvent = event;
        Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F));
        Bukkit.broadcastMessage(FracturedCore.getMessages().get(Messages.COMMAND_EVENT_RUN).replace("%event%", event.getColor() + event.getName()).replace("%time%", String.valueOf(event.getTime())));

        // todo fixme
        runCratesEvent();
    }

    public static void terminateEvent()
    {
        runningEvent = null;
    }

    // todo: make more dynamic
    public static void runCratesEvent() {
        Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(FracturedCore.class), () -> {
            // 250 radius of spawn
            int min = -250;
            int max = 250;
            Location spawn = WorldManager.getSpawn();

            if (spawn.getWorld() == null) {
                return;
            }

            Location location = spawn.clone().add(
                    ThreadLocalRandom.current().nextInt(min, max + 1), 0,
                    ThreadLocalRandom.current().nextInt(min, max + 1)
            );

            Block block = location.getBlock();
            block.setType(Material.CHEST);

            if (block.getState() instanceof Chest chest) {
                scatter(chest.getInventory());
            }

            Bukkit.broadcastMessage(Utils.color("&6&l!!! &fA &ecrate &fhas been dropped at &bX: " + Math.round(location.getX()) + " Y: 80 Z: " + Math.round(location.getZ()) + "! &6&l!!!"));
        });
    }

    public static void scatter(Inventory inv)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> added = new HashSet<>();

        for (int slot = 0; slot < inv.getSize(); slot++)
        {
            LootItem randomItem = crateLoot.get(random.nextInt(crateLoot.size()));

            if (added.contains(randomItem)) continue;
            added.add(randomItem);

            if (randomItem.shouldFill(random)) inv.setItem(slot, randomItem.getItem());
        }
    }
}
