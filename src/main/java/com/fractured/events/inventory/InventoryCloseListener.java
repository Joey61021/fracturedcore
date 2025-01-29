package com.fractured.events.inventory;

import com.fractured.managers.message.MessageManager;
import com.fractured.util.globals.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getView().getTitle().toLowerCase().contains("select team")) {
            MessageManager.sendMessage(player, Messages.TEAM_INVENTORY_CLOSED);
        }
    }
}
