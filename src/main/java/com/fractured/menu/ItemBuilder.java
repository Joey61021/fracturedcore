package com.fractured.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder
{
    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material)
    {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder name(String name)
    {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder lore(String... lore)
    {
        meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemStack build()
    {
        item.setItemMeta(meta);
        return item;
    }
}
