package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener
{
    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());
        Team team = user.getTeam();

        event.setQuitMessage(Utils.color((team != null ? team.color() : "&7") + player.getName() + " &fhas disconnected"));
    }
}
