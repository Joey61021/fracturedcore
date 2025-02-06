package com.fractured.shields;

import com.fractured.FracturedCore;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShieldManager implements Listener {

    public static Set<Shield> shields = new HashSet<>();
    public static Map<Shield, Set<PooledBlock>> pooledBlocks = new HashMap<>();

    public static int MAX_RADIUS = 20;
    public static int MIN_RADIUS = 5;

    @EventHandler
    public static void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Block block = event.getBlock();

        // Block is not a shield
        if (!item.getType().equals(Material.BEACON) || item.getItemMeta() == null || !item.getItemMeta().getDisplayName().toLowerCase().contains("shield"))
        {
            return;
        }

        ArmorStand armorStand = (ArmorStand) block.getWorld().spawnEntity(block.getLocation().add(.5, -1, .5), EntityType.ARMOR_STAND); // Center ArmorStand
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
        armorStand.setCustomName(Utils.color(ShieldState.AWAITING_CHANGE.getName()));

        block.setMetadata("shield", new FixedMetadataValue(JavaPlugin.getPlugin(FracturedCore.class), true));

        Shield shield = new Shield(block, 5, armorStand);
        shields.add(shield);

        initParameter(shield, 5);
        block.getWorld().strikeLightningEffect(block.getLocation());
        player.sendMessage(FracturedCore.getMessages().get(Messages.SHIELD_PLACE));
    }

    @EventHandler
    public static void onBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();

        if (!block.getType().equals(Material.BEACON) || !block.hasMetadata("shield"))
        {
            return;
        }

        Shield shield = getShield(block);
        if (shield == null)
        {
            return;
        }

        if (pooledBlocks.containsKey(shield))
        {
            removeParameter(shield);
        }

        shield.getArmorStand().remove();
        shields.remove(shield);
    }

    @EventHandler
    public static void onInteract(PlayerInteractEvent event)
    {
        Block clicked = event.getClickedBlock();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || clicked == null || clicked.getType() != Material.BEACON)
        {
            return;
        }

        Shield shield = getShield(clicked);

        if (shield == null)
        {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (player.isSneaking())
        {
            shield.setState(ShieldState.ACTIVE);
            removeParameter(shield);

            player.sendMessage(FracturedCore.getMessages().get(Messages.SHIELD_LOCKED));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            return;
        }

        int newRadius = shield.getRadius()+1 > MAX_RADIUS ? MIN_RADIUS : shield.getRadius()+1;
        player.sendMessage(FracturedCore.getMessages().get(Messages.SHIELD_CHANGE_RADIUS).replace("%radius%", String.valueOf(newRadius)));
        shield.setRadius(newRadius);

        removeParameter(shield);
        initParameter(shield, newRadius);
    }

    public static Shield getShield(Block block)
    {
        for (Shield shield : shields)
        {
            if (shield.getBlock().getLocation().equals(block.getLocation()))
            {
                return shield;
            }
        }

        // If no shields were found
        return null;
    }

    public static void initParameter(Shield shield, int radius) {
        Location shieldLoc = shield.getBlock().getLocation();

        Set<PooledBlock> blocks = new HashSet<>();
        fetchBlockRegion(blocks, shieldLoc.add(0, -1, 0), radius); // -1 to get floor

        pooledBlocks.put(shield, blocks);
        blocks.forEach(block -> block.getBlock().setType(Material.RED_CONCRETE));
    }

    public static void fetchBlockRegion(Set<PooledBlock> blocks, Location location, int radius)
    {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        World world = location.getWorld();

        // Draw the four sides of the square
        Block block;
        for (int i = -radius; i <= radius; i++) {
            block = world.getBlockAt(x + i, y, z - radius);  // Top edge
            blocks.add(new PooledBlock(block));

            block = world.getBlockAt(x + i, y, z + radius); // Bottom edge
            blocks.add(new PooledBlock(block));

            block = world.getBlockAt(x - radius, y, z + i); // Left edge
            blocks.add(new PooledBlock(block));

            block = world.getBlockAt(x + radius, y, z + i); // Right edge
            blocks.add(new PooledBlock(block));
        }
    }

    public static void removeParameter(Shield shield)
    {
        if (!pooledBlocks.containsKey(shield))
        {
            return;
        }

        Set<PooledBlock> blocks = pooledBlocks.get(shield);
        pooledBlocks.remove(shield);

        blocks.forEach(block -> {
            Block b = block.getBlock();
            b.setType(block.getOriginalType(), false); // false prevents physics updates
            b.setBlockData(block.getOriginalBlockData());
        });
    }
}
