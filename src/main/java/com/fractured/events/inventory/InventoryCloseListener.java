package com.fractured.events.inventory;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener
{

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        Player player = (Player) event.getPlayer();

        // todo migrate to MenuManager registry
        if (event.getView().getTitle().toLowerCase().contains("select team"))
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.TEAM_INVENTORY_CLOSED));
        }
    }
}
