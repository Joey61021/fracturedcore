package com.fractured.events.inventory;

import com.fractured.enums.Message;
import com.fractured.utilities.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getView().getTitle().toLowerCase().contains("select team")) {
            player.sendMessage(Utils.Color(Message.TEAM_INVENTORY_CLOSED.getMessage()));
        }
    }
}
