package com.fractured.events.world;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.team.ClaimManager;
import com.fractured.team.Claim;
import com.fractured.team.TeamCache;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import com.fractured.team.Team;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WorldManager implements Listener
{
    private static final World OVER_WORLD;
    private static Location SPAWN; // fixme use OVER_WORLD.getWorldSpawn() or sometrin glke that
    private static Location BEACON;

    private static final String DEFAULT_WORLD_PATH = "locations.default_world";
    private static final String BEACON_PATH = "locations.beacon";
    private static final String SPAWN_PATH = "locations.spawn";

    // This is for overworld, not nether or anything like that
    public static final int MAX_HEIGHT = 320;
    public static final int MIN_HEIGHT = -64;

    static
    {
        Config config = FracturedCore.getFracturedConfig();

        OVER_WORLD = Bukkit.getWorld(config.getString(DEFAULT_WORLD_PATH));

        if (OVER_WORLD == null)
        {
            throw new IllegalArgumentException("Unrecognized world at " + DEFAULT_WORLD_PATH);
        } else
        {
            SPAWN = getLocation(config, SPAWN_PATH);
            BEACON = getLocation(config, BEACON_PATH);
        }
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

    public static boolean setLocation(Config settings, String key, Location location) {
        if (!settings.isSet(key)) {
            return false;
        }

        settings.set(key + ".x", location.getX());
        settings.set(key + ".y", location.getY());
        settings.set(key + ".z", location.getZ());
        settings.set(key + ".yaw", location.getYaw());
        settings.set(key + ".pitch", location.getPitch());
        settings.set(key + ".world", location.getWorld().getName());
        settings.save();
        return true;
    }

    /**
     * This method does three things,
     * 1) Generates new team borders and sets the world border
     * 2) Updates beacon location in the config and in the computer
     * 3) Reloads team claims by recommitting them to the database and then reloading the ClaimManager completely.
     * @param radius Positive
     */
    public static void generateTeamBorders(int radius, Location location)
    {
        World world = location.getWorld();

        if (world == null)
        {
            throw new IllegalStateException("Cannot generate team borders in a null world!");
        }

        int x = location.getBlockX();
        int y = location.getBlockY() - 1;
        int z = location.getBlockZ();

        // todo add this to the chunk populator so it auto generates?
        for (int a = 0; a < y - MIN_HEIGHT; a++)
        {
            world.getBlockAt(x, y - a, z).setType(Material.BEDROCK);

            for (int b = 0; b < radius; b++)
            {
                world.getBlockAt(x + 1 + b, y - a, z - 1).setType(Material.RED_CONCRETE);
                world.getBlockAt(x + 1 + b, y - a, z).setType(Material.BEDROCK);
                world.getBlockAt(x + 1 + b, y - a, z + 1).setType(Material.BLUE_CONCRETE);
            }

            for (int b = 0; b < radius; b++)
            {
                world.getBlockAt(x - 1, y - a, z + 1 + b).setType(Material.YELLOW_CONCRETE);
                world.getBlockAt(x, y - a, z + 1 + b).setType(Material.BEDROCK);
                world.getBlockAt(x + 1, y - a, z + 1 + b).setType(Material.BLUE_CONCRETE);
            }

            for (int b = 0; b < radius; b++)
            {
                world.getBlockAt(x - 1 - b, y - a, z - 1).setType(Material.LIME_CONCRETE);
                world.getBlockAt(x - 1 - b, y - a, z).setType(Material.BEDROCK);
                world.getBlockAt(x - 1 - b, y - a, z + 1).setType(Material.YELLOW_CONCRETE);
            }

            for (int b = 0; b < radius; b++)
            {
                world.getBlockAt(x - 1, y - a, z - 1 - b).setType(Material.LIME_CONCRETE);
                world.getBlockAt(x, y - a, z - 1 - b).setType(Material.BEDROCK);
                world.getBlockAt(x + 1, y - a, z - 1 - b).setType(Material.RED_CONCRETE);
            }
        }

        world.getBlockAt(x, y, z).setType(Material.WHITE_STAINED_GLASS);
        world.getBlockAt(x, y - 1, z).setType(Material.BEACON);

        for (int a = 0; a < 3; a++)
        {
            for (int b = 0; b < 3; b++)
            {
                world.getBlockAt(x - 1 + a, y - 2, z - 1 + b).setType(Material.IRON_BLOCK);
            }
        }

        // TeamCache.getTeam cannot return null
//        Claim yellow = new Claim(TeamCache.getTeam("yellow"), x - 1, z + 1, x - radius, z + radius);
//        Claim green = new Claim(TeamCache.getTeam("green"), x - 1, z - 1, x - radius, z - radius);
//        Claim red = new Claim(TeamCache.getTeam("red"), x + 1, z - 1, x + radius, z - radius);
//        Claim blue = new Claim(TeamCache.getTeam("blue"), x + 1, z + 1, x + radius, z + radius);
//
//        ClaimManager.newClaim();

        //FracturedCore.getStorage().setClaims(yellow, green, red, blue);
        setLocation(FracturedCore.getFracturedConfig(), "locations.beacon", BEACON = new Location(world, x, y, z));

        // world border
        world.getWorldBorder().setCenter(BEACON);
        world.getWorldBorder().setSize(2 * radius);
    }

    public static void extendTeamBorders(int size)
    {
        // todo
    }

    public static Location getSpawn()
    {
        return SPAWN;
    }

    private static <E extends BlockEvent & Cancellable> void onBlockChange(Player player, E event)
    {
        if (player.getWorld() != OVER_WORLD || player.getGameMode() == GameMode.CREATIVE)
        {
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

        Location loc = event.getBlock().getLocation();

        Claim claim = ClaimManager.getClaim(loc);

        // The behavior here is changed slightly from the original.
        // If the enemy team is null, the event will not be cancelled, the original did cancel these events.
        if (claim != null && claim.getTeam() != team)
        {
            if (claim.getTeam().isOffline())
            {
                event.setCancelled(true);
                player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_TEAM_OFFLINE));
            } else
            {
                // Alert the enemy team
                claim.getTeam().alert("A block was changed in your claim at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")!");
            }
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

        if (player.getWorld() != OVER_WORLD || player.getGameMode() == GameMode.CREATIVE)
        {
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

        Block clicked = event.getClickedBlock();

        if (event.getClickedBlock() == null || clicked.getType() == Material.AIR)
        {
            return;
        }

        ItemStack item = event.getItem();

        if (clicked.getWorld() == BEACON.getWorld() &&
            item != null &&
            item.getType().equals(Material.PRISMARINE_SHARD) &&
            event.getItem().getItemMeta() != null &&
            event.getItem().getItemMeta().hasEnchant(Enchantment.UNBREAKING) &&
            clicked.getLocation().distance(BEACON) < 3)
        {
            // todo subtract just one
            player.setItemInHand(null);
            event.getClickedBlock().setType(team.beacon());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        }

        Location loc = event.getClickedBlock().getLocation();
        Claim claim = ClaimManager.getClaim(loc);

        // The behavior here is changed slightly from the original.
        // If the enemy team is null, the event will not be cancelled, the original did cancel these events.
        if (claim != null && claim.getTeam() != team)
        {
            if (claim.getTeam().isOffline())
            {
                event.setCancelled(true);
                player.sendMessage(FracturedCore.getMessages().get(Messages.REGION_TEAM_OFFLINE));
            } else
            {
                // todo config messages
                // Alert the enemy team
                claim.getTeam().alert("There is activity in your claim at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")!");
            }
        }
    }
}
