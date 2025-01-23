package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.User;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.fractured.util.Utils.color;

public class LeaveListener implements Listener
{
    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null)
        {
            event.setQuitMessage(color("&7" + player.getName() + " &fhas disconnected"));
        } else
        {
            team.getOnlineMembers().remove(player);
            event.setQuitMessage(team.color() + player.getName() + ChatColor.WHITE + " has disconnected");
        }
    }
}
