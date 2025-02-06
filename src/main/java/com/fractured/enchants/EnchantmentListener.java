package com.fractured.enchants;

import com.fractured.FracturedCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public final class EnchantmentListener implements Listener
{

    // For the soul bound enchant, temporarily keep the items for when the player respawns.
    public static Map<UUID, Set<ItemStack>> pooledItems = new HashMap<>();

    @EventHandler
    public static void onPrepare(PrepareItemEnchantEvent event)
    {
        ItemMeta meta = event.getItem().getItemMeta();

        if (meta == null)
        {
            return;
        }

        // if the item has custom enchantments on it, it's already enchanted:
        if (meta.getPersistentDataContainer().has(EnchantmentManager.FRACTURED_ENCHANTMENTS))
        {
            event.setCancelled(true);
        }
    }

    public static boolean hasEnchantment(ItemStack item, CustomEnchantment enchant) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        PersistentDataContainer enchants = pdc.get(EnchantmentManager.FRACTURED_ENCHANTMENTS, PersistentDataType.TAG_CONTAINER);

        if (enchants == null) {
            return false;
        }

        return enchants.has(enchant.key(), PersistentDataType.INTEGER);
    }

    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player attacker))
        {
            return;
        }

        ItemStack item = attacker.getEquipment().getItemInMainHand();
        Entity attacked = event.getEntity();

        if (item.getType().equals(Material.AIR) || item.getItemMeta() == null)
        {
            return;
        }

        Random rand = new Random();

        //todo fixme - levels
        if (hasEnchantment(item, CustomEnchantment.CONDUCTANCE))
        {
            double chance = 50;

            if ((rand.nextDouble() * 100) < chance)
            {
                event.setDamage(event.getDamage() + 5);
                attacked.getWorld().strikeLightning(attacked.getLocation());
            }
            return;
        }

        // These potion effects should only work for other players
        if (!(attacked instanceof Player))
        {
            return;
        }

        if (hasEnchantment(item, CustomEnchantment.VENOMOUS))
        {
            double chance = 50;

            if ((rand.nextDouble() * 100) < chance)
            {
                ((Player) attacked).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 60, 1));
            }
            return;
        }

        if (hasEnchantment(item, CustomEnchantment.WITHEROUS))
        {
            double chance = 50;

            if ((rand.nextDouble() * 100) < chance)
            {
                ((Player) attacked).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 5 * 60, 1));
            }
        }
    }

    @EventHandler
    public static void onBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        ItemStack item = event.getPlayer().getEquipment().getItemInMainHand();

        if (item.getType().equals(Material.AIR) || item.getItemMeta() == null)
        {
            return;
        }

        // todo: levels
        if (hasEnchantment(item, CustomEnchantment.AUTO_SMELT))
        {
            Material drop = switch (block.getType()) {
                case IRON_ORE, DEEPSLATE_IRON_ORE -> Material.IRON_INGOT;
                case GOLD_ORE, DEEPSLATE_GOLD_ORE -> Material.GOLD_INGOT;
                case COPPER_ORE, DEEPSLATE_COPPER_ORE -> Material.COPPER_INGOT;
                default -> Material.AIR;
            };

            // Block mined is not an ore since you can't break air
            if (drop.equals(Material.AIR))
            {
                return;
            }

            event.setDropItems(false);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(drop));
            return;
        }

        if (hasEnchantment(item, CustomEnchantment.TIMBER) && block.getType().name().toLowerCase().contains("log"))
        {
            Set<Block> blocks = new HashSet<>();
            // Radius should be determined by the level, 3*level. For this case lets say the level is 3, so 3*3
            getNearbyBlocks(false, block, 3*3, blocks);

            blocks.forEach(Block::breakNaturally);
            blocks.clear();
            return;
        }

        if (hasEnchantment(item, CustomEnchantment.CHUNKER))
        {
            Set<Block> blocks = new HashSet<>();
            getNearbyBlocks(true, block, 2, blocks);

            blocks.forEach(Block::breakNaturally);
            blocks.clear();
        }
    }

    @EventHandler
    public static void onDeath(PlayerDeathEvent event)
    {
        Set<ItemStack> items = new HashSet<>();

        for (ItemStack item : event.getDrops())
        {
            if (item == null || !item.hasItemMeta())
            {
                continue;
            }

            if (hasEnchantment(item, CustomEnchantment.SOUL_BOUND))
            {
                items.add(item);
            }
        }

        pooledItems.put(event.getPlayer().getUniqueId(), items);
        event.getDrops().removeAll(items);
    }

    @EventHandler
    public static void onRespawn(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();

        if (!pooledItems.containsKey(event.getPlayer().getUniqueId()))
        {
            return;
        }

        Inventory inv = player.getInventory();
        pooledItems.get(player.getUniqueId()).forEach(inv::addItem);
        pooledItems.remove(player.getUniqueId());
    }

    @EventHandler
    public static void onShoot(ProjectileLaunchEvent event)
    {
        if (!(event.getEntity() instanceof Arrow arrow) || !(event.getEntity().getShooter() instanceof Player player))
        {
            return;
        }

        // e.g. bow
        ItemStack item = player.getActiveItem();

        if ((!item.getType().equals(Material.BOW) && !item.getType().equals(Material.CROSSBOW)) || !hasEnchantment(item, CustomEnchantment.AIM_BOT))
        {
            return;
        }

        startHomingArrow(player, arrow);
    }

    public static void startHomingArrow(Player shooter, Arrow arrow)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (arrow.isDead() || arrow.isInBlock() || arrow.isOnGround())
                {
                    cancel(); // Stop the task if the arrow is no longer in flight
                    return;
                }

                Entity target = getNearestTarget(shooter, arrow.getLocation());

                if (target != null) {
                    Vector direction = target.getLocation().toVector().subtract(arrow.getLocation().toVector());

                    if (direction.lengthSquared() > 0) {
                        direction.normalize().multiply(arrow.getVelocity().length());
                        arrow.setVelocity(direction);
                    }
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(FracturedCore.class), 1L, 1L);
    }

    public static Player getNearestTarget(Player shooter, Location location)
    {
        Player nearestTarget = null;

        int maxRadius = 50;
        double closestDistance = Integer.MAX_VALUE;

        Collection<Player> players = location.getNearbyPlayers(maxRadius, 10, maxRadius);

        // No targets found besides shooter
        if (players.size() <= 1)
        {
            return null;
        }

        for (Player targets : players) {
            if (targets.equals(shooter) || !targets.getWorld().equals(location.getWorld()))
            {
                continue; // Skip the shooter and players in different worlds
            }

            double distance = targets.getLocation().distance(location);
            if (distance < maxRadius && distance < closestDistance)
            {
                closestDistance = distance;
                nearestTarget = targets;
            }
        }

        return nearestTarget;
    }

    public static void getNearbyBlocks(boolean checkType, Block block, int radius, Set<Block> blocks) {
        int bx = block.getX();
        int by = block.getY();
        int bz = block.getZ();

        World world = block.getWorld();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = world.getBlockAt(bx + x, by + y, bz + z);

                    // Check if within spherical radius
                    if ((relative.getType().equals(block.getType()) || checkType) && relative.getLocation().distance(block.getLocation()) <= radius) {
                        blocks.add(relative);
                    }
                }
            }
        }
    }
}
