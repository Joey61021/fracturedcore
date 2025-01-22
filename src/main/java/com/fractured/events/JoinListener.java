package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Tab
        player.setPlayerListHeader(Utils.Color("&eFractured &f| &e1.21.4 SMP"));
        player.setPlayerListFooter(Utils.Color("&7Your team: None"));

        String pooledTeam = FracturedCore.getDatabase.getString("teams." + player.getUniqueId());
        if (pooledTeam == null) {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.teleport(LocationManager.getLocation("locations.spawn"));
            TeamManager.displayGUI(player);
            player.setPlayerListName(Utils.Color("&7" + player.getName()));
            event.setJoinMessage(Utils.Color("&7" + player.getName() + " &fhas connected"));
            return;
        }

        Teams team = TeamManager.applyTeam(player, pooledTeam);
        event.setJoinMessage(Utils.Color(team.getColor() + player.getName() + " &fhas connected"));
    }
}
