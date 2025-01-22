package com.fractured.events.world;

import com.fractured.commands.BuildCmd;
import com.fractured.commands.BypassRegionsCmd;
import com.fractured.enums.AlertReason;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!BuildCmd.build.contains(player) && player.getGameMode() == GameMode.CREATIVE) {
            MessageManager.sendMessage(player, Message.UNABLE_TO_BUILD);
            event.setCancelled(true);
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || !player.getWorld().getName().equalsIgnoreCase("world")) {
            return;
        }

        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            event.setCancelled(true);
            return;
        }

        if (!BypassRegionsCmd.bypass.contains(player) && !LocationManager.isInRegion(event.getBlock().getLocation(), team.getPos1(), team.getPos2())) {
            Teams enemyTeam = LocationManager.getEnemyTeam(event.getBlock().getLocation(), team);
            if (enemyTeam == null) {
                event.setCancelled(true);
                return;
            }

            if (enemyTeam.getPlayers().size() < 1) {
                event.setCancelled(true);
                MessageManager.sendMessage(player, Message.REGION_TEAM_OFFLINE);
                return;
            }

            TeamManager.alertTeam(player, enemyTeam, event.getBlock().getLocation(), AlertReason.BLOCK_PLACE);
        }
    }
}
