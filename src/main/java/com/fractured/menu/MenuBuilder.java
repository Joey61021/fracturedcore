package com.fractured.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Used only for menus built and stored once
 */
public class MenuBuilder
{
    private final MenuCallback callbacks;
    private final Inventory inventory;

    public MenuBuilder(int size, String title)
    {
        callbacks = new MenuCallback();
        inventory = Bukkit.createInventory(null, size, title);
    }

    public void register(int slot, ItemStack item, InventoryCallback callback)
    {
        inventory.setItem(slot, item);
        callbacks.register(slot, callback);
    }

    public MenuCallback buildMenuCallback()
    {
        return callbacks;
    }

    public Inventory buildInventory()
    {
        return inventory;
    }
}
