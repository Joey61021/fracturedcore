package com.fractured.enums;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Upgrades {
    HELMET_PROTECTION(0, "&bHelmet Protection", new ItemStack(Material.IRON_HELMET), "helmet-protection", 5, Material.DIAMOND, 1);

    @Getter private final String display;
    @Getter private final ItemStack item;
    @Getter private final int slot;
    @Getter private final String upgradeValue;
    @Getter private final int maxVal;
    @Getter private final Material costItem;
    @Getter private final int costIncrement;

    Upgrades(int slot, String display, ItemStack item, String upgradeValue, int maxVal, Material costItem, int costIncrement) {
        this.slot = slot;
        this.display = display;
        this.item = item;
        this.upgradeValue = upgradeValue;
        this.maxVal = maxVal;
        this.costItem = costItem;
        this.costIncrement = costIncrement;
    }
}
