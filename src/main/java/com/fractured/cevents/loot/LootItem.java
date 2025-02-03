package com.fractured.cevents.loot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LootItem {

    public ItemStack item;
    public double chance;

    public LootItem(Material material, int amount, int chance)
    {
        this.item = new ItemStack(material, amount);
        this.chance = chance;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public double chance()
    {
        return this.chance;
    }

    public boolean shouldFill(Random random) {
        return random.nextDouble() < chance;
    }
}
