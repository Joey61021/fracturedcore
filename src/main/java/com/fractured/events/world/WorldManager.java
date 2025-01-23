package com.fractured.events.world;

import com.fractured.FracturedCore;
import com.fractured.team.TeamClaim;
import com.fractured.user.User;
import com.fractured.util.Config;
import com.fractured.util.globals.Messages;
import com.fractured.team.Team;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WorldManager implements Listener
{
    private static World OVER_WORLD;
    private static Location SPAWN;

    static
    {
        Config config = FracturedCore.getFracturedConfig();

        OVER_WORLD = Bukkit.getWorld(config.getString("locations.over_world"));

        if (OVER_WORLD == null)
        {
            throw new IllegalArgumentException("Unrecognized world at locations.over_world");
        } else
        {
            SPAWN = new Location(OVER_WORLD,
                    config.getDouble("locations.spawn.x"),
                    config.getDouble("locations.spawn.y"),
                    config.getDouble("locations.spawn.z"),
                    (float) config.getDouble("locations.spawn.yaw"),
                    (float) config.getDouble("locations.spawn.pitch"));
        }
    }

    public static Location getSpawn()
    {
        return SPAWN;
    }

    private <E extends BlockEvent & Cancellable> void onBlockChange(Player player, E event)
    {
        if (player.getWorld() != OVER_WORLD || player.getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());
        Team team = user.getTeam();

        // The player can't do anything without a team
        if (team == null)
        {
            event.setCancelled(true);
            return;
        }

        Location loc = event.getBlock().getLocation();

        TeamClaim claim = FracturedCore.getClaimManager().getClaim(loc.getBlockX(), loc.getBlockZ());

        // The behavior here is changed slightly from the original.
        // If the enemy team is null, the event will not be cancelled, the original did cancel these events.
        if (claim != null && claim.getTeam() != team)
        {
            if (claim.getTeam().isOffline())
            {
                event.setCancelled(true);
                player.sendMessage(Messages.REGION_TEAM_OFFLINE);
            } else
            {
                // Alert the enemy team
                claim.getTeam().alert("There is activity in your claim at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        onBlockChange(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        onBlockChange(event.getPlayer(), event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (player.getWorld() != OVER_WORLD || player.getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());
        Team team = user.getTeam();

        // The player can't do anything without a team
        if (team == null)
        {
            event.setCancelled(true);
            return;
        }

        Block clicked = event.getClickedBlock();
        if (clicked == null || clicked.getType() == Material.AIR)
        {
            return; // don't continue, we don't need to make any checks for this case
        }

        Location loc = event.getClickedBlock().getLocation();

        TeamClaim claim = FracturedCore.getClaimManager().getClaim(loc.getBlockX(), loc.getBlockZ());

        // The behavior here is changed slightly from the original.
        // If the enemy team is null, the event will not be cancelled, the original did cancel these events.
        if (claim != null && claim.getTeam() != team)
        {
            if (claim.getTeam().isOffline())
            {
                event.setCancelled(true);
                player.sendMessage(Messages.REGION_TEAM_OFFLINE);
            } else
            {
                // Alert the enemy team
                claim.getTeam().alert("There is activity in your claim at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
            }
        }
    }
}
