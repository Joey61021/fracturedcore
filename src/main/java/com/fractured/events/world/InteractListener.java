package com.fractured.events.world;

import com.fractured.commands.BuildCmd;
import com.fractured.enums.AlertReason;
import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!BuildCmd.build.contains(player) && player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(Utils.Color(Message.UNABLE_TO_BUILD.getMessage()));
            event.setCancelled(true);
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || !player.getWorld().getName().equalsIgnoreCase("world")) {
            return;
        }

        Teams team = TeamManager.getTeam(player);

        if (team == null || event.getClickedBlock() == null) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedBlock().getLocation().distance(LocationManager.beaconLoc) < 3 && event.getItem() != null && event.getItem().getType().equals(Material.PRISMARINE_SHARD) && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
            player.setItemInHand(null);
            event.getClickedBlock().setType(team.getBeaconMaterial());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        }

        if (event.getClickedBlock().getType().equals(Material.CHEST)) {
            if (!LocationManager.isInRegion(event.getClickedBlock().getLocation(), team.getPos1(), team.getPos2())) {
                Teams enemyTeam = LocationManager.getEnemyTeam(team, event.getClickedBlock().getLocation());
                if (enemyTeam == null) {
                    event.setCancelled(true);
                    return;
                }

                if (enemyTeam.getPlayers().size() < 1) {
                    event.setCancelled(true);
                    player.sendMessage(Utils.Color(Message.REGION_TEAM_OFFLINE.getMessage()));
                    return;
                }

                TeamManager.alertTeam(player, enemyTeam, event.getClickedBlock().getLocation(), AlertReason.CHEST_OPEN);
            }
        }
    }
}
