package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SleepListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        if (FracturedCore.getSettings.getBoolean("global.one-player-sleep")) {
            Bukkit.getServer().getScheduler().runTaskLater(FracturedCore.instance, () -> {
                if (event.getPlayer().isSleeping()) {
                    event.getPlayer().getWorld().setTime(0L);
                    event.getPlayer().getWorld().setStorm(false);
                    event.getPlayer().getWorld().setThundering(false);
                }
            }, 100L);
        }
    }
}
