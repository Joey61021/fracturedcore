package com.fractured.enchants;

import com.fractured.FracturedCore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EnchantmentManager implements Listener
{
    private static final NamespacedKey FRACTURED_ENCHANTMENTS = FracturedCore.newNamespacedKey("fractured_enchantments");

    /**
     * @return 0 success, 1 if above max level, 2 if enchant cannot be applied to this item
     */
    public static int enchant(ItemStack item, CustomEnchantment enchant, int level)
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
        }

        // This overrides whatever was already there, so if the enchant was already there at a lower level
        enchants.set(enchant.key(), PersistentDataType.INTEGER, level);
        pdc.set(FRACTURED_ENCHANTMENTS, PersistentDataType.TAG_CONTAINER, enchants);

        // Reload the lore
        List<String> lore = new ArrayList<>();

        for (NamespacedKey e : enchants.getKeys())
        {
            String display = CustomEnchantment.getByKey(e).display();
            // todo roman numeral
            lore.add(display + " " + enchants.get(e, PersistentDataType.INTEGER));
        }
        // Save lore
        meta.setLore(lore);

        // Save everything
        item.setItemMeta(meta);
        return 0;
    }

    @EventHandler
    public static void onPrepare(PrepareItemEnchantEvent event)
    {
        ItemMeta meta = event.getItem().getItemMeta();

        if (meta == null)
        {
            return;
        }

        // if the item has custom enchantments on it, it's already enchanted:
        if (meta.getPersistentDataContainer().has(FRACTURED_ENCHANTMENTS))
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

        PersistentDataContainer enchants = pdc.get(FRACTURED_ENCHANTMENTS, PersistentDataType.TAG_CONTAINER);

        if (enchants == null) {
            return false;
        }

        return enchants.has(enchant.key(), PersistentDataType.INTEGER);
    }

    @EventHandler
    public static void onBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        ItemStack item = event.getPlayer().getItemInHand();

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

    public static void getNearbyBlocks(boolean checkType, Block block, int radius, Set<Block> blocks) {
        int bx = block.getX();
        int by = block.getY();
        int bz = block.getZ();

        World world = block.getWorld();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = world.getBlockAt(bx + x, by + y, bz + z);

                    // Check if within spherical radius (optional)
                    if ((relative.getType().equals(block.getType()) || checkType) &&relative.getLocation().distance(block.getLocation()) <= radius) {
                        blocks.add(relative);

                    }
                }
            }
        }
    }
}
