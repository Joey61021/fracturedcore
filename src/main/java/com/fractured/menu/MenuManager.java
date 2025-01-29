package com.fractured.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MenuManager implements Listener
{
    private static Map<String, Consumer<InventoryClickEvent>> callbacks;

    public MenuManager()
    {
        if (callbacks != null)
        {
            throw new IllegalStateException("MenuManager has already been initialized");
        }
        callbacks = new HashMap<>();
    }

    public static Inventory register(int size, String title, Consumer<InventoryClickEvent> callback)
    {
        Inventory inventory = Bukkit.createInventory(null, size, title);

        callbacks.put(title, callback);

        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        InventoryView inventory = event.getView();

        var foo = callbacks.get(inventory.getTitle());

        if (foo != null)
        {
            foo.accept(event);
        }
    }
}
