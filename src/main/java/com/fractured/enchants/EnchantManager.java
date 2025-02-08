package com.fractured.enchants;

import com.fractured.FracturedCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static com.fractured.util.Utils.romanNumeral;

/**
 * Items that wish to carry a custom enchantment must pass through the {@link EnchantManager}.
 * When an item is enchanted, it is given through persistent data container a fractured_enchantments
 * tag, and under this tag as children are the actual enchantments.<p>
 * <code>
 * fractured_enchantments: {
 *   auto_smelt: 1,
 *   chunker: 2,
 * }</code><p>
 * This is exactly how PaperMC by default handles enchantments, only under the
 * components.minecraft:enchantments tag instead of fractured_enchantments
 * @author Cory Torode
 */
public final class EnchantManager implements Listener
{
    private static final NamespacedKey FRACTURED_ENCHANTMENTS = FracturedCore.newNamespacedKey("ench");

    public static final NamespacedKey ENTITY_DAMAGE_BY_ENTITY_EVENT = FracturedCore.newNamespacedKey("dmg");
    public static final NamespacedKey BLOCK_BREAK_EVENT = FracturedCore.newNamespacedKey("brk");
    public static final NamespacedKey PROJECTILE_LAUNCH_EVENT = FracturedCore.newNamespacedKey("proj");

    private static final EnchantCallbackHolder<EntityDamageByEntityEvent> damageCallbacks = new EnchantCallbackHolder<>();
    private static final EnchantCallbackHolder<BlockBreakEvent> blockBreakCallbacks = new EnchantCallbackHolder<>();
    private static final EnchantCallbackHolder<ProjectileLaunchEvent> projectileLaunchCallbacks = new EnchantCallbackHolder<>();

    static
    {
        damageCallbacks.register(Enchant.LIFE_STEAL, EnchantManager::lifesteal);
        damageCallbacks.register(Enchant.VENOMOUS, EnchantManager::venomous);
        damageCallbacks.register(Enchant.WITHEROUS, EnchantManager::witherous);
        damageCallbacks.register(Enchant.SHRED, EnchantManager::shred);
        damageCallbacks.register(Enchant.CONDUCTANCE, EnchantManager::conductance);

        blockBreakCallbacks.register(Enchant.TIMBER, EnchantManager::timber);
        blockBreakCallbacks.register(Enchant.AUTO_SMELT, EnchantManager::autoSmelt);
        blockBreakCallbacks.register(Enchant.CHUNKER, EnchantManager::chunker);

        projectileLaunchCallbacks.register(Enchant.AIM_BOT, EnchantManager::aimbot);
        projectileLaunchCallbacks.register(Enchant.DRAW, EnchantManager::draw);
    }

    /**
     * @return 0 success, 1 if above max level, 2 if enchant cannot be applied to this item
     */
    public static int enchant(ItemStack item, Enchant enchant, int level)
    {
        // Validate material
        if (enchant.maxLevel() < level)
        {
            return 1;
        }

        if (!enchant.isAssignableTo(item.getType()))
        {
            return 2;
        }

        // Meta should not be null, the Material was already checked
        ItemMeta meta = item.getItemMeta();

        meta.setEnchantmentGlintOverride(true);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        PersistentDataContainer enchants = pdc.get(FRACTURED_ENCHANTMENTS, PersistentDataType.TAG_CONTAINER);

        if (enchants == null)
        {
            enchants = pdc.getAdapterContext().newPersistentDataContainer();

            if (enchant.event() == null)
            {
                // This overrides whatever was already there, so if the enchant was already there at a lower level
                enchants.set(enchant.key(), PersistentDataType.INTEGER, level);
            } else
            {
                PersistentDataContainer event = enchants.getAdapterContext().newPersistentDataContainer();

                event.set(enchant.key(), PersistentDataType.INTEGER, level);

                enchants.set(enchant.event(), PersistentDataType.TAG_CONTAINER, event);
            }
        } else
        {
            if (enchant.event() == null)
            {
                // This overrides whatever was already there, so if the enchant was already there at a lower level
                enchants.set(enchant.key(), PersistentDataType.INTEGER, level);
            } else
            {
                PersistentDataContainer event = enchants.get(enchant.event(), PersistentDataType.TAG_CONTAINER);

                if (event == null)
                {
                    event = enchants.getAdapterContext().newPersistentDataContainer();
                }

                event.set(enchant.key(), PersistentDataType.INTEGER, level);

                enchants.set(enchant.event(), PersistentDataType.TAG_CONTAINER, event);
            }
        }
        pdc.set(FRACTURED_ENCHANTMENTS, PersistentDataType.TAG_CONTAINER, enchants);

        // Reload the lore
        List<String> lore = new ArrayList<>();

        for (NamespacedKey key0 : enchants.getKeys())
        {
            if (enchants.has(key0, PersistentDataType.INTEGER))
            {
                lore.add(Enchant.getByKey(key0).display() + " " + romanNumeral(enchants.get(key0, PersistentDataType.INTEGER)));
            } else
            {
                PersistentDataContainer event = enchants.get(key0, PersistentDataType.TAG_CONTAINER);

                for (NamespacedKey key1 : event.getKeys())
                {
                    lore.add(Enchant.getByKey(key1).display() + " " + romanNumeral(event.get(key1, PersistentDataType.INTEGER)));
                }
            }
        }
        // Save lore
        meta.setLore(lore);

        // Save everything
        item.setItemMeta(meta);
        return 0;
    }

    private static void lifesteal(EntityDamageByEntityEvent event, int level)
    {
        LivingEntity target = ((LivingEntity) event.getEntity());
        LivingEntity damager = ((LivingEntity) event.getDamager());

        LifestealRunnable runner = LifestealRunnable.get(damager, target);

        if (runner != null)
        {
            // reset the timer, a new hit was fired
            runner.restart();
        } else
        {
            // init a timer for the first hit
            runner = new LifestealRunnable(((LivingEntity) event.getEntity()), damager);
            runner.start();

            LifestealRunnable.put(runner);
        }
        // borrow health from the target
        runner.borrowHealth(0.5);
    }

    public static void conductance(EntityDamageByEntityEvent event, int level)
    {
        Entity attacked = event.getEntity();

        if (!(attacked instanceof LivingEntity))
        {
            return;
        }

        double chance = .5;

        if (Math.random() < chance)
        {
            if (event.getDamage() < 8)
            {
                event.setDamage(8);
                attacked.getWorld().strikeLightning(attacked.getLocation());
            }
        }
    }

    public static void venomous(EntityDamageByEntityEvent event, int level)
    {
        Entity attacked = event.getEntity();

        if (!(attacked instanceof LivingEntity))
        {
            return;
        }

        double chance = .5;

        if (Math.random() < chance)
        {
            ((LivingEntity) attacked).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1 * 20, 1));
        }
    }

    public static void witherous(EntityDamageByEntityEvent event, int level)
    {
        Entity attacked = event.getEntity();

        if (!(attacked instanceof LivingEntity))
        {
            return;
        }

        double chance = .5;

        if (Math.random() < chance)
        {
            ((LivingEntity) attacked).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1 * 20, 1));
        }
    }

    public static void shred(EntityDamageByEntityEvent event, int level)
    {
        Entity attacked = event.getEntity();

        if (!(attacked instanceof Player))
        {
            return;
        }

        ItemStack[] armor = ((Player) attacked).getInventory().getArmorContents();

        for (int i = 0; i < armor.length; ++i)
        {
            ItemStack item = armor[i];

            if (item != null)
            {
                item.setDurability((short) (armor[i].getDurability() - level + item.getEnchantmentLevel(Enchantment.UNBREAKING)));
            }
        }
    }

    public static void autoSmelt(BlockBreakEvent event, int level)
    {
        Block block = event.getBlock();

        Material drop = switch (block.getType())
        {
            case IRON_ORE, DEEPSLATE_IRON_ORE -> Material.IRON_INGOT;
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> Material.GOLD_INGOT;
            case COPPER_ORE, DEEPSLATE_COPPER_ORE -> Material.COPPER_INGOT;
            default -> null;
        };

        // Block mined is not an ore since you can't break air
        if (drop == null)
        {
            return;
        }

        // todo fortune
        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(drop));
    }

    public static void timber(BlockBreakEvent event, int level)
    {
        Block block = event.getBlock();

        Set<Block> blocks = new HashSet<>();
        getNearbyBlocks(false, block, 3 * level, blocks);

        blocks.forEach(Block::breakNaturally);
        blocks.clear();
    }

    public static void chunker(BlockBreakEvent event, int level)
    {
        Block block = event.getBlock();

        Set<Block> blocks = new HashSet<>();
        getNearbyBlocks(true, block, 2, blocks);

        blocks.forEach(Block::breakNaturally);
        blocks.clear();
    }

    public static void aimbot(ProjectileLaunchEvent event, int level)
    {
        startHomingArrow((Player) event.getEntity().getShooter(), (Arrow) event.getEntity());
    }

    public static void draw(ProjectileLaunchEvent event, int level)
    {
        Arrow arrow = (Arrow) event.getEntity();

        arrow.setVelocity(arrow.getVelocity().multiply(1 + level * 0.5));
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
        }.runTaskTimer(JavaPlugin.getPlugin(FracturedCore.class), 5L, 5L);
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

    // For the soul bound enchant, temporarily keep the items for when the player respawns.
    public static Map<UUID, List<ItemStack>> pooledItems = new HashMap<>();

    @EventHandler
    public static void onPrepare(PrepareItemEnchantEvent event)
    {
        ItemMeta meta = event.getItem().getItemMeta();

        if (meta == null)
        {
            return;
        }

        // if the item has custom enchantments on it, it's already enchanted:
        if (meta.getPersistentDataContainer().has(EnchantManager.FRACTURED_ENCHANTMENTS))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onPrepare(PrepareGrindstoneEvent event)
    {
        // TODO
    }

    private static PersistentDataContainer getEventHandles(ItemMeta meta, NamespacedKey event)
    {
        if (meta == null)
        {
            return null;
        }

        PersistentDataContainer enchants = meta.getPersistentDataContainer().get(FRACTURED_ENCHANTMENTS, PersistentDataType.TAG_CONTAINER);

        if (enchants == null)
        {
            return null;
        }

        return enchants.get(event, PersistentDataType.TAG_CONTAINER);
    }

    /**
     * Handles lifesteal, venomous, wither, shred, and conductance
     */
    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof LivingEntity attacker))
        {
            return;
        }

        EntityEquipment equipment = attacker.getEquipment();

        if (equipment == null)
        {
            return;
        }

        PersistentDataContainer eventCallbacks = getEventHandles(equipment.getItemInMainHand().getItemMeta(), ENTITY_DAMAGE_BY_ENTITY_EVENT);

        if (eventCallbacks == null)
        {
            return;
        }

        for (NamespacedKey key : eventCallbacks.getKeys())
        {
            damageCallbacks.get(key).accept(event, eventCallbacks.get(key, PersistentDataType.INTEGER));
        }
    }

    /**
     * Autosmelt, timber, and chunker
     */
    @EventHandler
    public static void onBreak(BlockBreakEvent event)
    {
        PersistentDataContainer eventCallbacks = getEventHandles(event.getPlayer().getInventory().getItemInMainHand().getItemMeta(), BLOCK_BREAK_EVENT);

        if (eventCallbacks == null)
        {
            return;
        }

        for (NamespacedKey key : eventCallbacks.getKeys())
        {
            blockBreakCallbacks.get(key).accept(event, eventCallbacks.get(key, PersistentDataType.INTEGER));
        }
    }

    public static void onDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();


    }

    @EventHandler
    public static void onDeath(PlayerDeathEvent event)
    {
        List<ItemStack> items = new ArrayList<>();

        for (ItemStack item : event.getDrops())
        {
            if (!item.hasItemMeta())
            {
                continue;
            }

            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            PersistentDataContainer enchants = pdc.get(FRACTURED_ENCHANTMENTS, PersistentDataType.TAG_CONTAINER);

            if (enchants != null && enchants.has(Enchant.SOUL_BOUND.key()))
            {
                event.getDrops().remove(item);
                items.add(item);
            }
        }

        pooledItems.put(event.getPlayer().getUniqueId(), items);
    }

    @EventHandler
    public static void onRespawn(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();
        List<ItemStack> items = pooledItems.remove(player.getUniqueId());

        if (items != null)
        {
            Inventory inv = player.getInventory();
            items.forEach(inv::addItem);
        }
    }

    @EventHandler
    public static void onShoot(ProjectileLaunchEvent event)
    {
        ProjectileSource entity = event.getEntity().getShooter();

        if (entity instanceof LivingEntity)
        {
            EntityEquipment equipment = ((LivingEntity) entity).getEquipment();

            if (equipment == null)
            {
                return;
            }

            PersistentDataContainer eventHandles = getEventHandles(equipment.getItemInMainHand().getItemMeta(), PROJECTILE_LAUNCH_EVENT);

            if (eventHandles == null)
            {
                return;
            }

            for (NamespacedKey key : eventHandles.getKeys())
            {
                projectileLaunchCallbacks.get(key).accept(event, eventHandles.get(key, PersistentDataType.INTEGER));
            }
        }
    }
}
