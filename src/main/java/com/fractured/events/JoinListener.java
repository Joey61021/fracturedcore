package com.fractured.events;

import com.fractured.FracturedCore;
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
        player.setPlayerListHeader(FracturedCore.getMessages().get(Messages.TAB_HEADER));

        User user = UserManager.getUser(player.getUniqueId());
        if (user == null)
        {
            // shouldn't ever be called
            player.kickPlayer(Messages.PLUGIN_INVALID_STATE);
            return;
        }

        Team team = user.getTeam();

        if (team == null)
        {
            player.setPlayerListFooter(FracturedCore.getMessages().get(Messages.TAB_FOOTER_NO_TEAM));
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.teleport(WorldManager.getSpawn());
            TeamCache.openMenu(player); // open team menu
            event.setJoinMessage(ChatColor.GRAY + player.getName() + ChatColor.WHITE + " has connected");
        }
        else
        {
            event.setJoinMessage(team.color() + player.getName() + ChatColor.WHITE + " has connected");
            team.memberJoined(player);
        }
    }
}
