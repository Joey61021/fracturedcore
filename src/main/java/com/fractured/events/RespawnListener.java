package com.fractured.events;

import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null) {
            return;
        }

        if (!event.isBedSpawn()) {
            event.setRespawnLocation(team.spawn());
        }

        //player.getInventory().setHelmet(team);
    }
}
