package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Teams team = TeamManager.getTeam(player);

        if (team != null && LocationManager.getEnemyTeam(team, event.getRespawnLocation()) != null) {
            event.setRespawnLocation(team.getSpawn());
            player.setBedSpawnLocation(team.getSpawn());
        }
    }
}
