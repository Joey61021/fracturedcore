
package com.fractured.events;

import com.fractured.enums.Teams;
import com.fractured.managers.TeamManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity victim = event.getEntity();

        if (!(victim instanceof Player)) {
            return;
        }

        Teams team = TeamManager.getTeam((Player) victim);

        if (team == null) {
            event.setCancelled(true);
        }
    }
}
