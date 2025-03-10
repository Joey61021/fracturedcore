package com.fractured.events;

import com.fractured.FracturedCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ExplodeListener implements Listener
{

    @EventHandler
    public static void onBlockLand(EntityChangeBlockEvent event)
    {
        Entity entity = event.getEntity();

        if (entity instanceof FallingBlock && entity.hasMetadata("tntDebris"))
        {
            event.setCancelled(true);
            entity.remove();
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event)
    {
        Location location = event.getLocation();
        World world = location.getWorld();

        if (world == null || world.equals(WorldManager.OVER_WORLD)) // only works in over world
        {
            return;
        }

        event.setCancelled(true);

        float size = event.getYield();
        world.createExplosion(location, size, false, false);

        Set<Block> blocks = new HashSet<>();
        getNearbyBlocks((int) size, location.add(0, -1, 0), blocks); // Get nearby blocks under player

        Random random = new Random();

        for (Block b : blocks)
        {
            if ((random.nextDouble() * 100) > 75)
            {
                continue;
            }

            Material material = b.getType();
            if (!canBeBroken(material)){
                continue;
            }

            FallingBlock fallingBlock = world.spawnFallingBlock(b.getLocation().add(0.5, 0, 0.5), material.createBlockData());

            double x = (random.nextDouble() - 0.25) * 1.25;
            double y = random.nextDouble() * 1.25 + 0.25;
            double z = (random.nextDouble() - 0.25) * 1.25;

            fallingBlock.setVelocity(new Vector(x, y, z));
            fallingBlock.setMetadata("tntDebris", new FixedMetadataValue(JavaPlugin.getPlugin(FracturedCore.class), true));
            fallingBlock.setDropItem(false);
        }
    }

    public static boolean canBeBroken(Material material)
    {
        return switch (material)
        {
            case BEDROCK, OBSIDIAN, BARRIER -> false;
            default -> true;
        };
    }

    public static void getNearbyBlocks(int radius, Location location, Set<Block> blocks)
    {
        int bx = (int) location.getX();
        int by = (int) location.getY();
        int bz = (int) location.getZ();

        for (int x = -radius; x <= radius; x++)
        {
            for (int y = -radius; y <= radius; y++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    Block relative = location.getWorld().getBlockAt(bx + x, by + y, bz + z);

                    // Check if within spherical radius
                    if (relative.getLocation().distance(location) <= radius)
                    {
                        blocks.add(relative);
                    }
                }
            }
        }
    }
}
