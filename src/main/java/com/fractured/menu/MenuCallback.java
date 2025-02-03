package com.fractured.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class MenuCallback implements InventoryCallback
{
    protected final Map<Integer, InventoryCallback> callbacks;

    public MenuCallback()
    {
        callbacks = new HashMap<>();
    }

    public void register(int slot, InventoryCallback callback)
    {
        callbacks.put(slot, callback);
    }

    @Override
    public void onClick(InventoryClickEvent event)
    {
        InventoryCallback slotCallback = callbacks.get(event.getSlot());

        if (slotCallback != null)
        {
            slotCallback.onClick(event);
        } else
        {
            event.setCancelled(true);
        }
    }
}
