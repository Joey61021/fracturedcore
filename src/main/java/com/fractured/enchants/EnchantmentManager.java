package com.fractured.enchants;

import com.fractured.FracturedCore;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

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

        // Add enchant
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
}
