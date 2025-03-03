package com.fractured.events.home;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class HomeManager implements Listener
{

    public static int MAX_HOMES = 5;
    public static Map<UUID, Set<Home>> homes = new HashMap<>();

    public static boolean exists(Player player, String name)
    {
        for (Home home : homes.get(player.getUniqueId()))
        {
            if (home.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }

        return false;
    }

    public static void addHome(Player player, String name)
    {
        Location loc = player.getLocation();
        homes.get(player.getUniqueId()).add(new Home(name, loc));

        // Add to homes.yml for later use
        Config homesConfig = FracturedCore.getHomes();
        String path = player.getUniqueId() + "." + name;
        homesConfig.set(path + ".world", loc.getWorld().getName());
        homesConfig.set(path + ".x", loc.getX());
        homesConfig.set(path + ".y", loc.getY());
        homesConfig.set(path + ".z", loc.getZ());
        homesConfig.save();
    }

    public static void delHome(Player player, String name)
    {
        Set<Home> playerHomes = new HashSet<>(); // Reinstate player homes

        for (Home home : homes.get(player.getUniqueId()))
        {
            if (home.getName().equalsIgnoreCase(name))
            {
                continue;
            }

            playerHomes.add(home);
        }

        homes.remove(player.getUniqueId());
        homes.put(player.getUniqueId(), playerHomes);

        Config homesConfig = FracturedCore.getHomes();
        homesConfig.set(player.getUniqueId() + "." + name, null);
        homesConfig.save();
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event)
    {
        homes.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        Config homesConfig = FracturedCore.getHomes();

        Set<Home> playerHomes = new HashSet<>();
        if (homesConfig.isSet(player.getUniqueId().toString()))
        {
            for (String section : homesConfig.getConfigurationSection(player.getUniqueId().toString()).getKeys(false))
            {
                String path = player.getUniqueId() + "." + section;
                String worldString = homesConfig.getString(path + ".world");
                World world = Bukkit.getWorld(worldString);

                if (world == null)
                {
                    Bukkit.getLogger().warning("Unable to find world: " + worldString);
                    continue;
                }

                Location location = new Location(world, homesConfig.getInt(path + ".x"), homesConfig.getInt(path + ".y"), homesConfig.getInt(path + ".z"));
                playerHomes.add(new Home(section, location));
            }
        }

        homes.put(player.getUniqueId(), playerHomes);
    }
}
