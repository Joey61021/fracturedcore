package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.team.TeamManager;
import com.fractured.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerListener implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null) {
            player.setFoodLevel(20);
        }
    }
}
