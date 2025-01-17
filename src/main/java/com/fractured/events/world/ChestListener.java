package com.fractured.events.world;

import com.fractured.enums.AlertReason;
import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {

    @EventHandler
    public void onChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE || event.getClickedBlock() == null || !event.getClickedBlock().getType().equals(Material.CHEST) || !player.getWorld().getName().equalsIgnoreCase("world")) {
            return;
        }

        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            event.setCancelled(true);
            return;
        }

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
