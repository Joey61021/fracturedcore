package com.fractured.events.inventory;

import com.fractured.enums.Settings;
import com.fractured.managers.SettingsManager;
import com.fractured.managers.TeamManager;
import com.fractured.enums.Teams;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Entity player = event.getWhoClicked();

        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR) && event.getSlot() == 0) {
            event.setCancelled(true);
            return;
        }

        if (event.getView().getTitle().toLowerCase().contains("select team")) {
            event.setCancelled(true);

            for (Teams team : Teams.values()) {
                if (Objects.equals(event.getCurrentItem(), team.getItem())) {
                    TeamManager.establishTeam((Player) player, team);
                    event.getView().close();
                }
            }
        }

        if (event.getView().getTitle().toLowerCase().contains("global settings")) {
            event.setCancelled(true);

            for (Settings setting : Settings.values()) {
                if (Objects.equals(event.getCurrentItem(), setting.getItem())) {
                    SettingsManager.toggleSetting((Player) player, setting);
                    event.getView().close();
                }
            }
        }
    }
}
