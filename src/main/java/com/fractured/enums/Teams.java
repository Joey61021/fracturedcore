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
    RED("Red", ChatColor.RED, new ItemStack(Material.RED_CONCRETE), 1, LocationManager.getLocation("red"), LocationManager.redPos1, LocationManager.redPos2,  Material.RED_STAINED_GLASS, new ArrayList<>()),
    BLUE("Blue", ChatColor.BLUE, new ItemStack(Material.BLUE_CONCRETE), 3, LocationManager.getLocation("blue"), LocationManager.bluePos1, LocationManager.bluePos2, Material.BLUE_STAINED_GLASS, new ArrayList<>()),
    GREEN("Green", ChatColor.GREEN, new ItemStack(Material.LIME_CONCRETE), 5, LocationManager.getLocation("green"), LocationManager.greenPos1, LocationManager.greenPos2, Material.LIME_STAINED_GLASS, new ArrayList<>()),
    YELLOW("Yellow", ChatColor.YELLOW, new ItemStack(Material.YELLOW_CONCRETE), 7, LocationManager.getLocation("yellow"), LocationManager.yellowPos1, LocationManager.yellowPos2, Material.YELLOW_STAINED_GLASS, new ArrayList<>());

    @Getter private final String name;
    @Getter private final ChatColor color;
    @Getter private final ItemStack item;
    @Getter private final int slot;
    @Getter private final Location spawn;
    @Getter private final Location pos1;
    @Getter private final Location pos2;
    @Getter private final Material beaconMaterial;
    @Getter private final ArrayList<Player> players;

    Teams(String name, ChatColor color, ItemStack item, int slot, Location spawn, Location pos1, Location pos2, Material beaconMaterial, ArrayList<Player> players) {
        this.name = name;
        this.color = color;
        this.item = item;
        this.slot = slot;
        this.spawn = spawn;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.beaconMaterial = beaconMaterial;
        this.players = players;
    }
}
