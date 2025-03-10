package com.fractured.kits;

import com.fractured.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Set;

public class Kit
{

    private int id;
    private String name;
    private Set<KitItem> items;
    private Material material;
    private ChatColor color;

    public Kit(int id, String name, Set<KitItem> items, Material material, ChatColor color)
    {
        this.id = id;
        this.name = name;
        this.items = items;
        this.material = material;
        this.color = color;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return Utils.color(name);
    }

    public Set<KitItem> getItems()
    {
        return items;
    }

    public Material getMaterial()
    {
        return material;
    }

    public ChatColor getColor()
    {
        return color;
    }
}
