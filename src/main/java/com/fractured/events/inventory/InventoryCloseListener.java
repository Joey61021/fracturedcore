package com.fractured.events.inventory;

import com.fractured.util.globals.Messages;
import com.fractured.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getView().getTitle().toLowerCase().contains("select team")) {
            player.sendMessage(Utils.color(Messages.TEAM_INVENTORY_CLOSED));
        }
    }
}
