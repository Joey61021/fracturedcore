package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.team.Claim;
import com.fractured.team.ClaimManager;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class ExplodeListener implements Listener {

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        if (event.getPlayer() == null) {
            return;
        }

        Player player = event.getPlayer();
        Team team = UserManager.getUser(event.getPlayer()).getTeam();
        Claim claim = ClaimManager.getClaim(event.getBlock().getLocation());

        if (team == null || claim == null)
        {
            event.setCancelled(true);
            return;
        }


        if (claim.getTeam() != null && claim.getTeam() != team && claim.getTeam().isOffline())
        {
            event.setCancelled(true);
            player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_TEAM_OFFLINE));
        }
    }
}
