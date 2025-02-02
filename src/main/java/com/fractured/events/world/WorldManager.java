package com.fractured.events.world;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.team.ClaimManager;
import com.fractured.team.Claim;
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
    private static Location BEACON;

    private static final String DEFAULT_WORLD_PATH = "locations.over_world";
    private static final String BEACON_PATH = "locations.beacon";

    static
    {
        Config config = FracturedCore.getFracturedConfig();

        OVER_WORLD = Bukkit.getWorld(config.getString(DEFAULT_WORLD_PATH));

        if (OVER_WORLD == null)
        {
            throw new IllegalArgumentException("Unable to find the over world world at " + DEFAULT_WORLD_PATH);
        } else
        {
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
     * Generates a team border from -x to +x starting at the location (x0, y0, z0) in the world, with the specified
     * materials on the left and right, and bedrock in the middle.
     * @param world not null
     */
    private static void generateXBorder(int dist, int x0, int y0, int z0, World world, Material left, Material right)
    {
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  2  2  2  2  2  2  2  2  2  2
        // .  .  .  .  .  b  @  @  @  @  @  @  @  @  @  @
        // .  .  .  .  .  .  1  1  1  1  1  1  1  1  1  1
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .

        int cx = x0 >> 4; // divide by 16
        int cz = z0 >> 4; // divide by 16

        Chunk chunk;
        int i;

        // dist >> 4 is the length of chunks we need to generate blocks
        // in, (minus any extra if the distance is not even), so we still
        // need to generate the tail end as well.
        for (i = 0; i < dist >> 4; ++i)
        {
            chunk = world.getChunkAt(cx + i, cz);

            for (int y = world.getMinHeight(); y <= y0; ++y)
            {

            }
        }

        chunk = world.getChunkAt(cx + i, cz);

        // finish the tail end
        for (int x = 0; x < dist % 16; ++x)
        {

        }
    }

    /**
     * Generates a team border from -x to +x starting at the location (x0, y0, z0) in the world, with the specified
     * materials on the left and right, and bedrock in the middle.
     * @param world not null
     */
    private static void generateZBorder(int dist, int x0, int y0, int z0, World world, Material left, Material right)
    {

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

        int x0 = location.getBlockX();
        int y0 = location.getBlockY() - 1;
        int z0 = location.getBlockZ();

        // . . . | . . . x = start generating
        // . . . | . . .
        // . . . x . . .
        // x - - + x - -
        // . . . | . . .
        // . . . | . . .
        // . . . x . . .

        generateXBorder(radius, x0, y0, z0, world, Material.RED_CONCRETE, Material.BLUE_CONCRETE);
        generateXBorder(radius, x0 - radius - 1, y0, z0, world, Material.GREEN_CONCRETE, Material.YELLOW_CONCRETE);
        generateZBorder(radius, x0, y0, z0, world, Material.BLUE_CONCRETE, Material.YELLOW_CONCRETE);
        generateZBorder(radius, x0, y0, z0 - radius - 1, world, Material.RED_CONCRETE, Material.GREEN_CONCRETE);

        // Generate beacon, glass
        world.getBlockAt(x0, y0 - 1, z0).setType(Material.BEACON);
        // (set beacon location)
        setLocation(FracturedCore.getFracturedConfig(), "locations.beacon", BEACON = new Location(world, x0, y0, z0));
        world.getBlockAt(x0, y0, z0).setType(Material.GLASS);

        // Generate iron blocks
        for (int x = 0; x < 3; x++)
        {
            for (int z = 0; z < 3; z++)
            {
                world.getBlockAt(x0 - 1 + x, y0 - 2, z0 - 1 + z).setType(Material.IRON_BLOCK);
            }
        }

        // Set world border
        world.getWorldBorder().setCenter(new Location(world, x0 + 0.5, y0, z0 + 0.5));
        world.getWorldBorder().setSize(2 * radius + 1);
    }

    public static void extendTeamBorders(int size)
    {


//        for (int a = 0; a < BEACON.getBlockY() + 1 - MIN_HEIGHT; a++)
//        {
//            world.getBlockAt(x, y - a, z).setType(Material.BEDROCK);
//
//            for (int b = 0; b < radius; b++)
//            {
//                world.getBlockAt(x + 1 + b, y - a, z - 1).setType(Material.RED_CONCRETE);
//                world.getBlockAt(x + 1 + b, y - a, z).setType(Material.BEDROCK);
//                world.getBlockAt(x + 1 + b, y - a, z + 1).setType(Material.BLUE_CONCRETE);
//            }
//
//            for (int b = 0; b < radius; b++)
//            {
//                world.getBlockAt(x - 1, y - a, z + 1 + b).setType(Material.YELLOW_CONCRETE);
//                world.getBlockAt(x, y - a, z + 1 + b).setType(Material.BEDROCK);
//                world.getBlockAt(x + 1, y - a, z + 1 + b).setType(Material.BLUE_CONCRETE);
//            }
//
//            for (int b = 0; b < radius; b++)
//            {
//                world.getBlockAt(x - 1 - b, y - a, z - 1).setType(Material.LIME_CONCRETE);
//                world.getBlockAt(x - 1 - b, y - a, z).setType(Material.BEDROCK);
//                world.getBlockAt(x - 1 - b, y - a, z + 1).setType(Material.YELLOW_CONCRETE);
//            }
//
//            for (int b = 0; b < radius; b++)
//            {
//                world.getBlockAt(x - 1, y - a, z - 1 - b).setType(Material.LIME_CONCRETE);
//                world.getBlockAt(x, y - a, z - 1 - b).setType(Material.BEDROCK);
//                world.getBlockAt(x + 1, y - a, z - 1 - b).setType(Material.RED_CONCRETE);
//            }
//        }
    }

    public static Location getSpawn()
    {
        return OVER_WORLD.getSpawnLocation();
    }

    private static <E extends BlockEvent & Cancellable> void onBlockChange(Player player, E event)
    {
        if (player.getGameMode() == GameMode.CREATIVE)
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

        if (player.getGameMode() == GameMode.CREATIVE)
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

        if (item == null || item.getType().equals(Material.AIR))
        {
            return;
        }

        if (item.getType().name().contains("helmet") && event.getAction().name().toLowerCase().contains("right"))
        {
            event.setCancelled(true);
            return;
        }

        if (clicked.getWorld() == BEACON.getWorld() &&
            item.getType().equals(Material.PRISMARINE_SHARD) &&
            event.getItem().getItemMeta() != null &&
            event.getItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) &&
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
