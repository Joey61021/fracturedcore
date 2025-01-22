package com.fractured.events.world;

import com.fractured.commands.BuildCmd;
import com.fractured.commands.BypassRegionsCmd;
import com.fractured.enums.AlertReason;
import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.RegionManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
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
                player.sendMessage(Utils.Color(Message.REGION_TEAM_OFFLINE.getMessage()));
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60 * 20, 0, false, false));
                return;
            }

            TeamManager.alertTeam(player, enemyTeam, event.getBlock().getLocation(), AlertReason.BLOCK_PLACE);
        }
    }
}
