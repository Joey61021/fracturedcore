package com.fractured.events;

import com.fractured.utilities.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Location loc = victim.getLocation();

        victim.sendMessage(Utils.Color("&7You died at (" + Math.round(loc.getX()) + ", " + Math.round(loc.getY()) + ", " + Math.round(loc.getZ()) + ")"));

        if (victim.getKiller() != null) {
            event.setDeathMessage(Utils.Color(victim.getDisplayName() + " &fwas killed by " + victim.getKiller().getDisplayName()));
            return;
        }
        event.setDeathMessage(Utils.Color(victim.getDisplayName() + " &fdied"));
    }
}
