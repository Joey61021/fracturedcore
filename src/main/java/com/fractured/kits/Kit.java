package com.fractured.kits;

import com.fractured.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Set;

public class Kit
{

    private int id;
    private String name;
    private int cooldown;
    private Set<KitItem> items;
    private Material material;
    private ChatColor color;

    public Kit(int id, String name, int cooldown, Set<KitItem> items)
    {
        this.id = id;
        this.name = name;
        this.cooldown = cooldown;
        this.items = items;
        // TODO -- configurable items, set default items for testing
        this.material = Material.BOW;
        this.color = ChatColor.GREEN;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return Utils.color(name);
    }

    public int getCooldown()
    {
        return cooldown;
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
