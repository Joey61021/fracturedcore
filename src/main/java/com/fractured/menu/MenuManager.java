package com.fractured.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;

public class MenuManager implements Listener
{
    private static Map<String, InventoryCallback> callbacks;

    public MenuManager()
    {
        // smh spigot
        if (callbacks != null)
        {
            throw new IllegalStateException("MenuManager has already been initialized");
        }
        callbacks = new HashMap<>();
    }

    /**
     * Register an inventory title to a callback that allows each slot to have an action
     */
    public static void register(String title, InventoryCallback callback)
    {
        callbacks.put(title, callback);
    }

    @EventHandler
    public static void onClick(InventoryClickEvent event)
    {
        InventoryView inventory = event.getView();

        var inventoryCallback = callbacks.get(inventory.getTitle());

        if (inventoryCallback != null)
        {
            if (event.getClickedInventory() != inventory.getTopInventory())
            {
                // Player can't click their own inventory and have actions occur
                return;
            }

            event.setCancelled(true);
            inventoryCallback.onClick(event);
        }
    }
}
