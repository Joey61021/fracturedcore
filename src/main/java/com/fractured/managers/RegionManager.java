package com.fractured.managers;

import com.fractured.FracturedCore;
import com.fractured.enums.Teams;
import com.fractured.utilities.Config;
import com.fractured.utilities.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RegionManager {

    public static Teams selectedTeam = Teams.RED;

    public static ItemStack getWand(Teams team) {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Utils.color("&6Region Wand &7: " + team.getColor() + team.getName()));
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    public static Teams cycleTeam(ItemStack item) {
        Teams team = Teams.values()[(selectedTeam.ordinal() + 1) % Teams.values().length];
        selectedTeam = team;

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color("&6Region Wand &7: " + team.getColor() + team.getName()));
        item.setItemMeta(meta);

        return team;
    }

    public static void selectRegion(boolean first, Location location) {
        Config settings = FracturedCore.getSettings;
        settings.set("regions." + selectedTeam.getName().toLowerCase() + ".pos" + (first ? "1" : "2") + ".x", location.getX());
        settings.set("regions." + selectedTeam.getName().toLowerCase() + ".pos" + (first ? "1" : "2") + ".y", first ? LocationManager.MIN_HEIGHT : LocationManager.MAX_HEIGHT);
        settings.set("regions." + selectedTeam.getName().toLowerCase() + ".pos" + (first ? "1" : "2") + ".z", location.getZ());
        settings.save();
    }
}
