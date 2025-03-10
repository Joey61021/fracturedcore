package com.fractured.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;

public class KitItem
{

    private Material material;
    private int amount;
    private String name;
    private ArrayList<Enchantment> enchantment = new ArrayList<>();

    public KitItem(Material material, int amount)
    {
        this.material = material;
        this.amount = amount;
        this.name = material.name();
    }

    public Material getMaterial()
    {
        return material;
    }

    public int getAmount()
    {
        return amount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void addEnchantment(Enchantment enchantment)
    {
        this.enchantment.add(enchantment);
    }
}
