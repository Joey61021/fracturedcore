package com.fractured.events;

import com.fractured.enums.Teams;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Teams team = TeamManager.getTeam(player);
        event.setQuitMessage(Utils.Color((team != null ? team.getColor() : "&7") + player.getName() + " &fhas disconnected"));
    }
}
