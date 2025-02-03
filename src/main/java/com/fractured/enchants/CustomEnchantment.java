package com.fractured.enchants;

import com.fractured.FracturedCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

public enum CustomEnchantment
{
    // Armor
    FIRE_RESISTANCE("Fire Resistance", "fire_resistance", GearType.ARMOR, 1),
    WATER_BREATHING("Water Breathing", "water_breathing", GearType.HELMET, 1),
    SPEED("Speed", "speed", GearType.BOOTS, 2),
    /**
     * Items stay with you when you die. For each teleport, the level is decremented.
     */
    SOUL_BOUND(ChatColor.RESET + ChatColor.GRAY.toString() + "Soul Bound", "soul_bound", GearType.ARMOR, 5),
    /**
     * If you kill an enemy, you gain some buff potion effects (armor)
     */
    RESTORATION(ChatColor.RESET + ChatColor.GRAY.toString() + "Restoration", "restoration", GearType.CHEST_PLATE, 3),

    // Axe
    /**
     * Cut down an entire tree (or a big part of it)
     */
    TIMBER(ChatColor.RESET + ChatColor.GRAY.toString() + "Timber", "timber", GearType.AXE, 3),

    // Pickaxe
    CHUNKER(ChatColor.RESET + ChatColor.GRAY.toString() + "Chunker", "chunker", GearType.PICKAXE, 2),
    AUTO_SMELT(ChatColor.RESET + ChatColor.GRAY.toString() + "Auto Smelt", "auto_smelt", GearType.PICKAXE, 1),

    // Sword
    /**
     * Steal a player's hearts
     */
    LIFE_STEAL(ChatColor.RESET + ChatColor.GRAY.toString() + "Life Steal", "life_steal", GearType.SWORD, 3),
    /**
     * Hit a player and they take damage repeatedly after. Essentially bleeding out
     */
    BLEED(ChatColor.RESET + ChatColor.GRAY.toString() + "Bleed", "bleed", GearType.SWORD, 3),
    /**
     * Inflict poison
     */
    VENOMOUS(ChatColor.RESET + ChatColor.GRAY.toString() + "Venomous", "venomous", GearType.SWORD, 2),
    /**
     * Inflict wither
     */
    WITHEROUS(ChatColor.RESET + ChatColor.GRAY.toString() + "Witherous", "witherous", GearType.SWORD, 2),
    /**
     * Deals more durability to your opponents armor
     */
    SHRED(ChatColor.RESET + ChatColor.GRAY.toString() + "Shred", "shred", GearType.SWORD, 2),
    // Potentially summon lightning on attack
    CONDUCTANCE(ChatColor.RESET + ChatColor.GRAY.toString() + "Conductance", "conductance", GearType.SWORD, 3),

    /**
     * Increases bow initial arrow velocity
     */
    DRAW(ChatColor.RESET + ChatColor.GRAY.toString() + "Draw", "draw", GearType.RANGED, 3),
    /**
     * Arrows automatically aim to a player
     */
    AIM_BOT(ChatColor.RESET + ChatColor.GRAY.toString() + "Aim Bot", "aim_bot", GearType.RANGED, 3)

    ;

    private static final Map<String, CustomEnchantment> ENCHANTMENTS = new HashMap<>();

    static
    {
        for (CustomEnchantment enchantment : values())
        {
            ENCHANTMENTS.put(enchantment.key().getKey(), enchantment);
        }
    }

    public static CustomEnchantment getByName(String name)
    {
        return ENCHANTMENTS.get(name);
    }

    public static CustomEnchantment getByKey(NamespacedKey key)
    {
        return getByName(key.getKey());
    }

    private final String display;
    private final NamespacedKey key;
    private final int allowedItems;
    private final int maxLevel;

    CustomEnchantment(String name, String key, int allowedItems, int maxLevel)
    {
        this.display = name;
        this.key = FracturedCore.newNamespacedKey(key);
        this.allowedItems = allowedItems;
        this.maxLevel = maxLevel;
    }

    public String display()
    {
        return display;
    }

    public NamespacedKey key()
    {
        return key;
    }

    public int maxLevel()
    {
        return maxLevel;
    }

    private static int getGearType(Material material)
    {
        return switch (material)
        {
            case Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET, Material.TURTLE_HELMET -> GearType.HELMET;
            case Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.ELYTRA -> GearType.CHEST_PLATE;
            case Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS -> GearType.LEGGINGS;
            case Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS -> GearType.BOOTS;
            case Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD -> GearType.SWORD;
            case Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL -> GearType.SHOVEL;
            case Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE -> GearType.AXE;
            case Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE -> GearType.PICKAXE;
            case Material.BOW -> GearType.BOW;
            case Material.CROSSBOW -> GearType.CROSSBOW;
            case Material.TRIDENT -> GearType.TRIDENT;
            default -> 0;
        };
    }

    public boolean isAssignableTo(Material material)
    {
        return (getGearType(material) & allowedItems) != 0;
    }
}
