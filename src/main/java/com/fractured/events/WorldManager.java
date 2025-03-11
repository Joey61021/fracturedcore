package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.team.Claim;
import com.fractured.team.ClaimManager;
import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class WorldManager implements Listener
{
    public static final World OVER_WORLD;

    public static Location SPAWN;
    public static Location SPAWN_POS1;
    public static Location SPAWN_POS2;
    public static Location END_ROOM; // End room to accommodate difficulties in relation to accessing the end

    private static final String DEFAULT_WORLD_PATH = "locations.over_world";

    private static final String SPAWN_PATH = "locations.spawn";
    private static final String SPAWN_POS1_PATH = "locations.spawn_pos1";
    private static final String SPAWN_POS2_PATH = "locations.spawn_pos2";
    private static final String END_ROOM_PATH = "locations.end_room";

    static
    {
        Config config = FracturedCore.getFracturedConfig();

        String path = config.getString(DEFAULT_WORLD_PATH);
        if (path == null)
        {
            throw new IllegalArgumentException("Invalid default world path at " + DEFAULT_WORLD_PATH);
        }

        OVER_WORLD = Bukkit.getWorld(path);

        if (OVER_WORLD == null)
        {
            throw new IllegalArgumentException("Unable to find the over world world at " + DEFAULT_WORLD_PATH);
        } else
        {
            SPAWN = getLocation(config, SPAWN_PATH);
            SPAWN_POS1 = getLocation(config, SPAWN_POS1_PATH);
            SPAWN_POS2 = getLocation(config, SPAWN_POS2_PATH);
            END_ROOM = getLocation(config, END_ROOM_PATH);

            OVER_WORLD.setSpawnLocation(SPAWN);
        }
    }

    public static boolean isInSpawn(Location location)
    {
        return isInRegion(location, SPAWN_POS1, SPAWN_POS2);
    }

    private static boolean isInRegion(Location loc, Location pos1, Location pos2)
    {
        if (loc.getWorld() != WorldManager.SPAWN.getWorld())
        {
            return false;
        }

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        return pos1.getWorld().equals(loc.getWorld()) && loc.getX() > minX && loc.getX() < maxX && loc.getY() > minY && loc.getY() < maxY && loc.getZ() > minZ && loc.getZ() < maxZ;
    }

    public static Location getLocation(Config config, String path)
    {
        String worldString = config.getString(path + ".world");
        World world;

        if (worldString == null)
        {
            world = OVER_WORLD;
        } else
        {
            world = Bukkit.getWorld(worldString);

            if (world == null)
            {
                world = OVER_WORLD;
                Bukkit.getLogger().warning("Unable to find world " + worldString + " from " + path);
            }
        }

        return new Location(world,
                config.getDouble(path + ".x", 0.0),
                config.getDouble(path + ".y", 64.0),
                config.getDouble(path + ".z", 0.0),
                (float) config.getDouble(path + ".yaw", 0.0),
                (float) config.getDouble(path + ".pitch", 0.0));
    }

    private static <E extends BlockEvent & Cancellable> void onBlockChange(Player player, E event)
    {
        if (player.getGameMode() == GameMode.CREATIVE || UserManager.getUser(player.getUniqueId()).getBypassRegions())
        {
            return;
        }

        Block block = event.getBlock();
        Location loc = block.getLocation();

        if (isInSpawn(loc) && block.getType() != Material.END_PORTAL_FRAME)
        {
            event.setCancelled(true);
            player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_SPAWN_REGION));
            return;
        }

        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        // The player can't do anything without a team
        if (team == null)
        {
            event.setCancelled(true);
            return;
        }

        Claim claim = ClaimManager.getClaim(loc);

        // The behavior here is changed slightly from the original.
        // If the enemy team is null, the event will not be cancelled, the original did cancel these events.
        if (claim != null && claim.getTeam() != team && !isInSpawn(loc))
        {
            if (claim.getShield())
            {
                //todo fixme config messages
                player.sendMessage(Utils.color("&cThis region is protected by a shield!"));
                event.setCancelled(true);
                return;
            }

            if (claim.getTeam().isOffline())
            {
                event.setCancelled(true);
                player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_TEAM_OFFLINE));
            } else
            {
                // If last alert location is less than 5 blocks away, cancel to prevent spam
                if (user.getLastAlert() != null && loc.distance(user.getLastAlert()) < 5)
                {
                    return;
                }
                // Alert the enemy team
                user.setLastAlert(loc);
                claim.getTeam().alert(FracturedCore.getMessages().get(Messages.REGION_ALERT_BLOCK_CHANGE)
                        .replace("%player%", player.getName())
                        .replace("%team%", team.getName())
                        .replace("%locx%", String.valueOf(loc.getBlockY()))
                        .replace("%locy%", String.valueOf(loc.getBlockY()))
                        .replace("%locz%", String.valueOf(loc.getBlockZ())));
                player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_TEAM_ALERTED).replace("%team%", team.getName()));
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 5, 0));
            }
        }
    }

    @EventHandler
    public static void onFood(FoodLevelChangeEvent event)
    {
        Player player = (Player) event.getEntity();
        Team team = UserManager.getUser(player.getUniqueId()).getTeam();

        if (team == null)
        {
            event.setFoodLevel(20);
            return; // Cancel food level change if the player is not in a team
        }

        Random rand = new Random(); // Reduce hunger depletion rate by 50%
        if (event.getEntity().getFoodLevel() > event.getFoodLevel() && rand.nextDouble() >= 0.5)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onSpawn(CreatureSpawnEvent event)
    {
        if (isInSpawn(event.getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onBreak(BlockBreakEvent event)
    {
        onBlockChange(event.getPlayer(), event);
    }

    @EventHandler
    public static void onPlace(BlockPlaceEvent event)
    {
        onBlockChange(event.getPlayer(), event);
    }

    @EventHandler
    public static void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (player.getGameMode() == GameMode.CREATIVE || (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR))
        {
            return;
        }

        if (!item.getType().equals(Material.AIR) && item.toString().toLowerCase().contains("helmet"))
        {
            event.setCancelled(true);
            return; // Prevent helmet equip
        }

        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        // The player can't do anything without a team
        if (team == null)
        {
            event.setCancelled(true);
            return;
        }

        Block clicked = event.getClickedBlock();

        if (clicked == null || clicked.getType() == Material.AIR || !(clicked.getState() instanceof Chest))
        {
            return;
        }

        Location loc = event.getClickedBlock().getLocation();
        Claim claim = ClaimManager.getClaim(loc);

        // The behavior here is changed slightly from the original.
        // If the enemy team is null, the event will not be cancelled, the original did cancel these events.
        if (!user.getBypassRegions() && claim != null && claim.getTeam() != team)
        {
            if (claim.getShield())
            {
                //todo fixme config messages
                player.sendMessage(Utils.color("&cThis region is protected by a shield!"));
                event.setCancelled(true);
                return;
            }

            if (claim.getTeam().isOffline())
            {
                event.setCancelled(true);
                player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_TEAM_OFFLINE));
            } else
            {
                // If last alert location is less than 5 blocks away, cancel to prevent spam
                if (user.getLastAlert() != null && loc.distance(user.getLastAlert()) < 5)
                {
                    return;
                }
                // Alert the enemy team
                user.setLastAlert(loc);
                claim.getTeam().alert(FracturedCore.getMessages().get(Messages.REGION_ALERT_ACTIVITY)
                        .replace("%player%", player.getName())
                        .replace("%team%", team.getName())
                        .replace("%locx%", String.valueOf(loc.getBlockY()))
                        .replace("%locy%", String.valueOf(loc.getBlockY()))
                        .replace("%locz%", String.valueOf(loc.getBlockZ())));
                player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_TEAM_ALERTED).replace("%team%", team.getName()));
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 5, 0));
            }
        }
    }
}
