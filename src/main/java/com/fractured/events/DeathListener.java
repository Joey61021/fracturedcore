package com.fractured.events;

import com.fractured.util.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location loc = player.getLocation();

        player.sendMessage(Utils.color("&7You died at (" + Math.round(loc.getX()) + ", " + Math.round(loc.getY()) + ", " + Math.round(loc.getZ()) + ")"));
    }
}
