package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.team.Claim;
import com.fractured.team.ClaimManager;
import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

import java.util.HashSet;
import java.util.Set;

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
    private static void generateXBorder(final int dist, int x0, int y0, int z0, World world, Material left, Material right)
    {
        // .  .  .  .  .  v  @  @  @  @  @  @  @  @  @  @  ^ -z
        // .  .  .  .  .  .  1  1  1  1  1  1  1  1  1  1  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  2  2  2  2  2  2  2  2  2  2  |
        // .  .  .  .  .  v  @  @  @  @  @  @  @  @  @  @  |
        // .  .  .  .  .  .  1  1  1  1  1  1  1  1  1  1  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  | v = (x0, z0) % 16   [for this case, v = (5,5)]
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  |
        // .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  V +z
        // <  -  -  -  -  -  -  -  -  -  -  -  -  -  -  >
        // -x                                          +x

        /* Where the player is in the chunk relative to the 0 corner of it.
           (THIS IS NOT THE SAME AS CHUNK COORDINATE, THESE SCALE THE SAME
           AS REGULAR COORDINATES) */
        // Relative coordinates
        int rx = x0 % 16;
        int rz = z0 % 16;

        if (rx < 0)
        {
            rx += 16;
        }

        if (rz < 0)
        {
            rz += 16;
        }

        // Chunk coordinates
        int cx = x0 >> 4; // divide by 16
        int cz = z0 >> 4; // divide by 16

        if (rz == 0)
        {
            // -z edge (We need to load 2 chunks)

            Chunk bedrock = world.getChunkAt(cx, cz);
            Chunk adjacent = world.getChunkAt(cx, cz - 1);

            // Less than one chunk
            if (rx + dist < 16)
            {

            }

            for (int x = rx + 1; x <= Math.min(15, rx + dist); ++x)
            {
                for (int y = world.getMinHeight() + 1; y <= y0; ++y)
                {
                    adjacent.getBlock(rx, y, 15).setType(left);
                    bedrock.getBlock(rx, y, 0).setType(Material.BEDROCK);
                    bedrock.getBlock(rx, y, 1).setType(right);
                }
            }
        } else if (rz == 15)
        {
            // +z edge (We need to load 2 chunks)

            Chunk bedrock = world.getChunkAt(cx, cz);
            Chunk adjacent = world.getChunkAt(cx, cz + 1);
        } else
        {
            Chunk chunk = world.getChunkAt(cx, cz);

            int rxAndX;

            for (int x = 1; x <= Math.min(15 - rx, dist); ++x)
            {
                rxAndX = rx + x;

                for (int y = world.getMinHeight() + 1; y <= y0; ++y)
                {
                    chunk.getBlock(rxAndX, y, rz - 1).setType(left);
                    chunk.getBlock(rxAndX, y, rz).setType(Material.BEDROCK);
                    chunk.getBlock(rxAndX, y, rz + 1).setType(right);
                }
            }

            int i;

            // Start at the next chunk, we already did one
            // (dist - rx) >> 4 is how many chunks we need to decorate.
            for (i = 1; i <= (dist - rx) >> 4; ++i)
            {
                chunk = world.getChunkAt(cx + i, cz);

                for (int x = 0; x < 16; ++x)
                {
                    for (int y = world.getMinHeight() + 1; y <= y0; ++y)
                    {
                        chunk.getBlock(x, y, rz - 1).setType(left);
                        chunk.getBlock(x, y, rz).setType(Material.BEDROCK);
                        chunk.getBlock(x, y, rz + 1).setType(right);
                    }
                }
            }

            chunk = world.getChunkAt(cx + i, cz);

            for (int x = 0; x < (dist - (15 - rx)) % 16; ++x)
            {
                for (int y = world.getMinHeight() + 1; y <= y0; ++y)
                {
                    chunk.getBlock(x, y, rz - 1).setType(left);
                    chunk.getBlock(x, y, rz).setType(Material.BEDROCK);
                    chunk.getBlock(x, y, rz + 1).setType(right);
                }
            }
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
        generateZBorder(radius, x0, y0, z0, world, Material.BLUE_CONCRETE, Material.YELLOW_CONCRETE);
        generateXBorder(radius, x0 - radius - 1, y0, z0, world, Material.GREEN_CONCRETE, Material.YELLOW_CONCRETE);
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

        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getItemInUse();

        // Item cannot be null
        if (item == null || item.getItemMeta() == null || item.getType().equals(Material.AIR))
        {
            return;
        }

        // todo fixme
        // Testing purposes only - enchant logic, remove when enchants are setup
        if (player.hasPermission(Permissions.COMMAND_WORLD_ADMIN) && item.getItemMeta().hasEnchants() && item.getItemMeta().hasEnchant(Enchantment.DURABILITY))
        {

            // CUSTOM ENCHANT - AUTOSMELT
            if (item.getType().equals(Material.NETHERITE_PICKAXE))
            {

                Material drop = switch (item.getType()) {
                    case IRON_ORE, DEEPSLATE_IRON_ORE -> Material.IRON_INGOT;
                    case GOLD_ORE, DEEPSLATE_GOLD_ORE -> Material.GOLD_INGOT;
                    case COPPER_ORE, DEEPSLATE_COPPER_ORE -> Material.COPPER_INGOT;
                    default -> Material.AIR;
                };
                event.setDropItems(false);
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(drop));
                return;
            }

            // CUSTOM ENCHANT - TREE HARVASTER
            if (item.getType().equals(Material.NETHERITE_AXE) && item.getType().name().toLowerCase().contains("log"))
            {
                Set<Block> blocks = new HashSet<>();
                // Radius is determined by the level, 3*level. For this case lets say the level is 2, so 3*2
                getNearbyBlocks(false, block, 3*2, blocks);

                for (Block b : blocks) {
                    b.setType(Material.AIR);
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(b.getType()));
                }

                blocks.clear();
            }

            // CUSTOM ENCHANT - BREAK 3x3x3
            if (item.getType().equals(Material.DIAMOND_PICKAXE))
            {
                Set<Block> blocks = new HashSet<>();
                getNearbyBlocks(true, block, 9, blocks);

                for (Block b : blocks) {
                    b.setType(Material.AIR);
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(b.getType()));
                }

                blocks.clear();
            }
        }
    }

    public static void getNearbyBlocks(boolean ignoreType, Block block, int radius, Set<Block> blocks)
    {
        if (blocks.size() >= radius)
        {
            return;
        }

        blocks.add(block);

        for (BlockFace face : BlockFace.values()) {
            Block relative = block.getRelative(face);
            if ((ignoreType || relative.getType().equals(block.getType())) && !blocks.contains(relative)) {
                getNearbyBlocks(true, relative, radius, blocks);
            }
        }
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
                // If last alert location is less than 5 blocks away, cancel to prevent spam
                if (user.getLastAlert() != null && player.getLocation().distance(user.getLastAlert()) < 10)
                {
                    return;
                }
                // Alert the enemy team
                user.setLastAlert(player.getLocation());
                claim.getTeam().alert("There is activity in your claim at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")!");
            }
        }
    }
}
