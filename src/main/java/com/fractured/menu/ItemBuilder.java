package com.fractured.menu;

import com.fractured.util.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

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
        meta.setDisplayName(Utils.color(name));
        return this;
    }

    public ItemBuilder lore(String... lore)
    {
        meta.setLore(Arrays.stream(lore).map(Utils::color).collect(Collectors.toList()));
        return this;
    }

    public ItemStack build()
    {
        item.setItemMeta(meta);
        return item;
    }
}
