package com.fractured.enchants;

import com.fractured.FracturedCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

import static com.fractured.enchants.GearType.*;
import static com.fractured.enchants.EnchantManager.BLOCK_BREAK_EVENT;
import static com.fractured.enchants.EnchantManager.ENTITY_DAMAGE_BY_ENTITY_EVENT;
import static com.fractured.enchants.EnchantManager.PROJECTILE_LAUNCH_EVENT;

public enum Enchant
{
    // Armor
    FIRE_RESISTANCE("Fire Resistance", null, "fire", ARMOR, 1),
    WATER_BREATHING("Water Breathing", null, "water", HELMET, 1),
    SPEED("Speed", null, "speed", BOOTS, 2),
    /**
     * Items stay with you when you die. For each teleport, the level is decremented.
     */
    SOUL_BOUND(ChatColor.RESET + ChatColor.GRAY.toString() + "Soul Bound", null, "bnd", ALL, 5),
    /**
     * If you kill an enemy, you gain some buff potion effects (armor)
     */
    RESTORATION(ChatColor.RESET + ChatColor.GRAY.toString() + "Restoration", null, "rest", CHEST_PLATE, 3),

    // Axe
    /**
     * Cut down an entire tree (or a big part of it)
     */
    TIMBER(ChatColor.RESET + ChatColor.GRAY.toString() + "Timber", BLOCK_BREAK_EVENT, "timb", AXE, 3),

    // Pickaxe
    CHUNKER(ChatColor.RESET + ChatColor.GRAY.toString() + "Chunker", BLOCK_BREAK_EVENT, "chnk", PICKAXE, 2),
    AUTO_SMELT(ChatColor.RESET + ChatColor.GRAY.toString() + "Auto Smelt", BLOCK_BREAK_EVENT, "smlt", PICKAXE, 1),

    // Sword
    /**
     * Steal a player's hearts
     */
    LIFE_STEAL(ChatColor.RESET + ChatColor.GRAY.toString() + "Life Steal", ENTITY_DAMAGE_BY_ENTITY_EVENT, "stl", GearType.SWORD, 3),
    /**
     * Inflict poison
     */
    VENOMOUS(ChatColor.RESET + ChatColor.GRAY.toString() + "Venomous",  ENTITY_DAMAGE_BY_ENTITY_EVENT, "vnm", GearType.SWORD, 2),
    /**
     * Inflict wither
     */
    WITHEROUS(ChatColor.RESET + ChatColor.GRAY.toString() + "Witherous",  ENTITY_DAMAGE_BY_ENTITY_EVENT, "wthr", GearType.SWORD, 2),
    /**
     * Deals more durability to your opponents armor
     */
    SHRED(ChatColor.RESET + ChatColor.GRAY.toString() + "Shred",  ENTITY_DAMAGE_BY_ENTITY_EVENT, "shred", GearType.SWORD, 2),
    // Potentially summon lightning on attack
    CONDUCTANCE(ChatColor.RESET + ChatColor.GRAY.toString() + "Conductance",  ENTITY_DAMAGE_BY_ENTITY_EVENT, "cnd", GearType.SWORD, 3),

    /**
     * Increases bow initial arrow velocity
     */
    DRAW(ChatColor.RESET + ChatColor.GRAY.toString() + "Draw", PROJECTILE_LAUNCH_EVENT, "drw", GearType.RANGED, 3),
    /**
     * Arrows automatically aim to a player
     */
    AIM_BOT(ChatColor.RESET + ChatColor.GRAY.toString() + "Aim Bot", PROJECTILE_LAUNCH_EVENT, "aim", GearType.RANGED, 3)

    ;

    private static final Map<String, Enchant> ENCHANTMENTS = new HashMap<>();

    static
    {
        for (Enchant enchant : values())
        {
            ENCHANTMENTS.put(enchant.key.getKey(), enchant);
        }
    }

    public static Enchant getByKey(NamespacedKey key)
    {
        return ENCHANTMENTS.get(key.getKey());
    }

    private final String display;
    private final NamespacedKey event;
    private final NamespacedKey key;
    private final int allowedItems;
    private final int maxLevel;

    Enchant(String name, NamespacedKey event, String key, int allowedItems, int maxLevel)
    {
        this.display = name;
        this.event = event;
        this.key = FracturedCore.newNamespacedKey(key);
        this.allowedItems = allowedItems;
        this.maxLevel = maxLevel;
    }

    public String display()
    {
        return display;
    }

    public NamespacedKey event()
    {
        return event;
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
            case Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET, Material.TURTLE_HELMET -> HELMET;
            case Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.ELYTRA -> CHEST_PLATE;
            case Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS -> LEGGINGS;
            case Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS -> BOOTS;
            case Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD -> SWORD;
            case Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL -> SHOVEL;
            case Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE -> AXE;
            case Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE -> PICKAXE;
            case Material.BOW -> BOW;
            case Material.CROSSBOW -> CROSSBOW;
            case Material.TRIDENT -> TRIDENT;
            default -> 0;
        };
    }

    public boolean isAssignableTo(Material material)
    {
        return (getGearType(material) & allowedItems) != 0;
    }
}
