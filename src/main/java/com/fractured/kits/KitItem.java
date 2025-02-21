package com.fractured.kits;

import org.bukkit.Material;

public class KitItem
{

    private Material material;
    private int amount;

    public KitItem(Material material, int amount)
    {
        this.material = material;
        this.amount = amount;
    }

    public Material getMaterial()
    {
        return material;
    }

    public int getAmount()
    {
        return amount;
    }
}
