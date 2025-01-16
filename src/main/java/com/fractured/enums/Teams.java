package com.fractured.enums;

import com.fractured.managers.LocationManager;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public enum Teams {
    RED("Red", ChatColor.RED, new ItemStack(Material.RED_CONCRETE), 1, new Location(LocationManager.world, 250.5, 64, 250.5), LocationManager.redPos1, LocationManager.redPos2, new ArrayList<>()),
    BLUE("Blue", ChatColor.BLUE, new ItemStack(Material.BLUE_CONCRETE), 3, new Location(LocationManager.world, 250, 64, -249.5), LocationManager.bluePos1, LocationManager.bluePos2, new ArrayList<>()),
    GREEN("Green", ChatColor.GREEN, new ItemStack(Material.LIME_CONCRETE), 5, new Location(LocationManager.world, -250.5, 71, -250.5), LocationManager.greenPos1, LocationManager.greenPos2, new ArrayList<>()),
    YELLOW("Yellow", ChatColor.YELLOW, new ItemStack(Material.YELLOW_CONCRETE), 7, new Location(LocationManager.world, -250.5, 64, 250.5), LocationManager.yellowPos1, LocationManager.yellowPos2, new ArrayList<>());

    @Getter private final String name;
    @Getter private final ChatColor color;
    @Getter private final ItemStack item;
    @Getter private final int slot;
    @Getter private final Location spawn;
    @Getter private final Location pos1;
    @Getter private final Location pos2;
    @Getter private final ArrayList<Player> players;

    Teams(String name, ChatColor color, ItemStack item, int slot, Location spawn, Location pos1, Location pos2, ArrayList<Player> players) {
        this.name = name;
        this.color = color;
        this.item = item;
        this.slot = slot;
        this.spawn = spawn;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.players = players;
    }
}
