package com.fractured.events.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public final class InventoryClickListener implements Listener
{

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event)
    {
        // can't move helmet
        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR) && event.getSlot() == 39)
        {
            event.setCancelled(true);
        }
    }
}
