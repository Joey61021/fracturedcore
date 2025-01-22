package com.fractured.managers;

import com.fractured.FracturedCore;
import com.fractured.enums.Settings;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import com.fractured.utilities.Config;
import com.fractured.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SettingsManager {

    public static boolean isToggled(Settings setting) {
        return FracturedCore.getSettings.getBoolean("global." + setting.getSettingsValue(), false);
    }

    public static void displayGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, Utils.color("&7Global Settings"));
        player.openInventory(inv);

        for (Settings setting : Settings.values()) {
            ItemStack item = setting.getItem();

            // Meta data
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.color(setting.getDisplay() + "&7 : " + (isToggled(setting) ? "&aON" : "&cOFF")));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Utils.color("&7Click to toggle setting"));
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.setItem(setting.getSlot(), item);
        }
    }

    public static void toggleSetting(Player player, Settings setting) {
        boolean val = !isToggled(setting);

        Config settings = FracturedCore.getSettings;
        settings.set("global." + setting.getSettingsValue(), val);
        settings.save();

        MessageManager.sendMessage(player, Message.CMD_SETTING_TOGGLE, (s) -> s.replace("%val%", setting.getSettingsValue()).replace("%bool%", val ? "&aON" : "&cOFF"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
    }

    public static boolean setLocation(String key, Location location) {
        Config settings = FracturedCore.getSettings;

        if (!settings.isSet("locations." + key)) {
            return false;
        }

        settings.set("locations." + key + ".x", location.getX());
        settings.set("locations." + key + ".y", location.getY());
        settings.set("locations." + key + ".z", location.getZ());
        settings.save();
        return true;
    }
}
