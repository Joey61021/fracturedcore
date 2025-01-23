package com.fractured.events.inventory;

import com.fractured.FracturedCore;
import com.fractured.enums.Settings;
import com.fractured.enums.Teams;
import com.fractured.enums.Upgrades;
import com.fractured.managers.SettingsManager;
import com.fractured.managers.TeamManager;
import com.fractured.managers.UpgradesManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Entity player = event.getWhoClicked();

        if (!(player instanceof Player) || event.getSlotType().equals(InventoryType.SlotType.ARMOR) && event.getSlot() == 39) {
            event.setCancelled(true);
            return;
        }

        if (event.getView().getTitle().toLowerCase().contains("select team")) {
            event.setCancelled(true);

            for (Teams team : Teams.values()) {
                if (Objects.equals(event.getCurrentItem(), team.getItem())) {
                    TeamManager.establishTeam((Player) player, team);
                    event.getView().close();
                    return;
                }
            }
            return;
        }

        if (event.getView().getTitle().toLowerCase().contains("global settings")) {
            event.setCancelled(true);

            for (Settings setting : Settings.values()) {
                if (Objects.equals(event.getCurrentItem(), setting.getItem())) {
                    SettingsManager.toggleSetting((Player) player, setting);
                    event.getView().close();
                    return;
                }
            }
            return;
        }

        Teams team = TeamManager.getTeam((Player) player);

        if (team != null && event.getView().getTitle().toLowerCase().contains("team upgrades")) {
            event.setCancelled(true);

            for (Upgrades upgrade : Upgrades.values()) {
                if (Objects.equals(event.getCurrentItem(), upgrade.getItem())) {

                    int upgradeLevel = FracturedCore.getSettings.getInt("upgrades." + team.getName().toLowerCase() + "." + upgrade.getUpgradeValue(), 1);
                    int amountRequired = upgrade.getCostIncrement() * upgradeLevel;

                    if (upgradeLevel >= upgrade.getMaxVal()) {
                        MessageManager.sendMessage(player, Message.UPGRADES_REACH_MAX_VAL);
                        return;
                    }

                    for (ItemStack item : ((Player) player).getInventory().getContents()) {
                        if (item != null && item.getType().equals(upgrade.getCostItem()) && item.getAmount() >= amountRequired) {
                            item.setAmount(item.getAmount() - amountRequired);
                            UpgradesManager.upgrade((Player) player, team, upgrade);
                            event.getView().close();
                            return;
                        }
                    }

                    MessageManager.sendMessage(player, Message.UPGRADES_NOT_ENOUGH);
                    return;
                }
            }
        }
    }
}
