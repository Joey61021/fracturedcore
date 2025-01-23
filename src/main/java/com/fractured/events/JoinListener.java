package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.events.world.WorldManager;
import com.fractured.team.TeamCache;
import com.fractured.user.User;
import com.fractured.util.Utils;
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
        player.setPlayerListHeader(Utils.color("&eFractured &f| &e1.21.4 SMP"));
        player.setPlayerListFooter(Utils.color("&7Your team: None")); // fixme

        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());

        if (user.getTeam() == null)
        {
            player.teleport(WorldManager.getSpawn());
            TeamCache.openMenu(player); // open team menu
            event.setJoinMessage(ChatColor.GRAY + player.getName() + ChatColor.WHITE + " has connected");
        } else
        {
            event.setJoinMessage(user.getTeam().color() + player.getName() + ChatColor.WHITE + " has connected");
        }
    }
}
