package com.fractured.enums;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Settings {
    FRIENDLY_FIRE(1, "&bFriendly Fire", new ItemStack(Material.BOW), "friendly-fire"),
    STARTER_ITEMS(7, "&6Starter Items", new ItemStack(Material.WOODEN_AXE), "starter-items");

    @Getter private final String display;
    @Getter private final ItemStack item;
    @Getter private final int slot;
    @Getter private final String settingsValue;

    Settings(int slot, String display, ItemStack item, String settingsValue) {
        this.slot = slot;
        this.display = display;
        this.item = item;
        this.settingsValue = settingsValue;
    }
}
