package com.fractured.events;

import com.fractured.events.world.WorldManager;
import com.fractured.team.Team;
import com.fractured.team.TeamCache;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class JoinListener implements Listener
{
    @EventHandler
    public static void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Tab
        player.setPlayerListHeader(Messages.TAB_LIST_HEADER);

        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null)
        {
            player.setPlayerListFooter(Messages.NO_TEAM_TAB_LIST_FOOTER);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setFireTicks(0);
            // don't do this because players already established will have their inventories cleared. They wont have a team because of the new database schema
            // player.getInventory().clear();
            player.teleport(WorldManager.getSpawn());
            TeamCache.openMenu(player); // open team menu
            event.setJoinMessage(ChatColor.GRAY + player.getName() + ChatColor.WHITE + " has connected");
        } else
        {
            event.setJoinMessage(team.color() + player.getName() + ChatColor.WHITE + " has connected");
            // add member? What happened to that here
            team.getOnlineMembers().add(player);
        }
    }
}
