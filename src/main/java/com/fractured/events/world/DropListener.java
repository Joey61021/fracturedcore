package com.fractured.events.world;

import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.RegionManager;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE && event.getItemDrop().getItemStack().getType() == Material.BLAZE_ROD && event.getItemDrop().getItemStack().hasItemMeta() && event.getItemDrop().getItemStack().getItemMeta().getDisplayName().toLowerCase().contains("region wand")) {
            event.setCancelled(true);
            Teams team = RegionManager.cycleTeam(event.getItemDrop().getItemStack());
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
            player.sendMessage(Utils.Color(Message.REGION_CYCLE_TEAM.getMessage().replace("%team%", team.getColor() + team.getName())));
        }
    }
}
