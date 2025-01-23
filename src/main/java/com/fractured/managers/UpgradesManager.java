package com.fractured.managers;

import com.fractured.FracturedCore;
import com.fractured.enums.Teams;
import com.fractured.enums.Upgrades;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import com.fractured.utilities.Config;
import com.fractured.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class UpgradesManager {

    public static int getLevel(Upgrades upgrade, Teams team) {
        return FracturedCore.getSettings.getInt("upgrades." + team.getName().toLowerCase() + "." + upgrade.getUpgradeValue(), 1);
    }

    public static void displayGUI(Player player, Teams team) {
        Inventory inv = Bukkit.createInventory(null, 9, Utils.color("&8Team upgrades"));
        player.openInventory(inv);

        for (Upgrades upgrade : Upgrades.values()) {
            ItemStack item = upgrade.getItem();

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.color(upgrade.getDisplay() + "&7 : &6" + getLevel(upgrade, team)));
            ArrayList<String> lore = new ArrayList<>();

            int level = FracturedCore.getSettings.getInt("upgrades." + team.getName().toLowerCase() + "." + upgrade.getUpgradeValue(), 1);
            lore.add(Utils.color(level >= upgrade.getMaxVal() ? "&cMaximum level has been reached!" : "&7Cost: &e" + level * upgrade.getCostIncrement() + " " + upgrade.getCostItem().toString()));

            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.setItem(upgrade.getSlot(), item);
        }
    }

    public static void upgrade(Player player, Teams team, Upgrades upgrade) {
        Config settings = FracturedCore.getSettings;
        int val = settings.getInt("upgrades." + team.getName().toLowerCase() + "." + upgrade.getUpgradeValue(), 1)+1;

        if (val > upgrade.getMaxVal()) {
            MessageManager.sendMessage(player, Message.UPGRADES_REACH_MAX_VAL);
            return;
        }

        settings.set("upgrades." + team.getName().toLowerCase() + "." + upgrade.getUpgradeValue(), val);
        settings.save();

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        MessageManager.sendMessage(player, Message.UPGRADES_UPGRADED, (s) -> s.replace("%upgrade%", upgrade.getDisplay()).replace("%level%", "" + val));

        if (upgrade == Upgrades.HELMET_PROTECTION) {
            for (Player players : team.getPlayers()) {
                players.getInventory().setHelmet(TeamManager.getHelmet(team));
            }
        }
    }
}
