package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.events.world.WorldManager;
import com.fractured.team.Team;
import com.fractured.team.TeamCache;
import com.fractured.user.User;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Tab
        player.setPlayerListHeader(Messages.TAB_LIST_HEADER);

        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null)
        {
            player.teleport(WorldManager.getSpawn());
            TeamCache.openMenu(player); // open team menu
            event.setJoinMessage(ChatColor.GRAY + player.getName() + ChatColor.WHITE + " has connected");
            player.setPlayerListFooter(Messages.NO_TEAM_TAB_LIST_FOOTER);
        } else
        {
            event.setJoinMessage(team.color() + player.getName() + ChatColor.WHITE + " has connected");
            player.setPlayerListFooter(Utils.color("&7Your team: " +  team.color() + team.getName() + " team"));
            team.getOnlineMembers().add(player);
        }
    }
}
