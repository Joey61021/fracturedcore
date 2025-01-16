package com.fractured.events.world;

import com.fractured.enums.AlertReason;
import com.fractured.enums.Message;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.enums.Teams;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.Team;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE || !player.getWorld().getName().equalsIgnoreCase("world")) {
            return;
        }

        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            event.setCancelled(true);
            return;
        }

        if (!LocationManager.isInRegion(event.getBlock().getLocation(), team.getPos1(), team.getPos2())) {
            Teams enemyTeam = LocationManager.getEnemyTeam(team, event.getBlock().getLocation());
            if (enemyTeam == null) {
                event.setCancelled(true);
                return;
            }

            if (enemyTeam.getPlayers().size() < 1) {
                event.setCancelled(true);
                player.sendMessage(Utils.Color(Message.REGION_TEAM_OFFLINE.getMessage()));
                return;
            }

            TeamManager.alertTeam(player, enemyTeam, event.getBlock().getLocation(), AlertReason.BLOCK_BREAK);
        }
    }
}
