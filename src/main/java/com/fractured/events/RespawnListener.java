package com.fractured.events;

import com.fractured.enums.Teams;
import com.fractured.managers.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            return;
        }

        player.getInventory().setHelmet(TeamManager.getHelmet(team));
    }
}
