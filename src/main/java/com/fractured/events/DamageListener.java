
package com.fractured.events;

import com.fractured.enums.Message;
import com.fractured.enums.Settings;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.SettingsManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class DamageListener implements Listener {

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        if (!(damager instanceof Player)) {
            return;
        }

        Teams team = TeamManager.getTeam((Player) damager);

        if (((Player) damager).getGameMode() != GameMode.SURVIVAL && team != null && LocationManager.getEnemyTeam(team, damager.getLocation()) != null) {
            damager.sendMessage(Utils.Color(Message.REGION_TEAM_OFFLINE.getMessage()));
            event.setCancelled(true);
        }

        if (!(victim instanceof Player)) {
            return;
        }

        Teams victimTeam = TeamManager.getTeam((Player) victim);
        Teams damagerTeam = TeamManager.getTeam((Player) damager);

        if (damagerTeam != null && victimTeam == damagerTeam && !SettingsManager.isToggled(Settings.FRIENDLY_FIRE)) {
            event.setCancelled(true);
            damager.sendMessage(Utils.Color(Message.FRIENDLY_FIRE_DISABLED.getMessage()));
        }
    }
}
