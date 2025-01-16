package com.fractured.events.inventory;

import com.fractured.managers.TeamManager;
import com.fractured.enums.Teams;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Entity player = event.getWhoClicked();
        if (!event.getView().getTitle().toLowerCase().contains("select team")) {
            return;
        }

        event.setCancelled(true);

        for (Teams team : Teams.values()) {
            if (Objects.equals(event.getCurrentItem(), team.getItem())) {
                TeamManager.establishTeam((Player) player, team);
                event.getView().close();
            }
        }
    }
}
