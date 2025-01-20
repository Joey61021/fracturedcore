package com.fractured.events;

import com.fractured.utilities.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class KillListener implements Listener {

    @EventHandler
    public void onJoin(EntityDeathEvent event) {
        Entity victim = event.getEntity();

        if (victim instanceof Wither) {
            ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.Color("&bFractured Shard"));
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Utils.Color("&7Insert this shard into the centre beacon to activate"));
            meta.setLore(lore);
            item.setItemMeta(meta);

            victim.getLocation().getWorld().dropItem(victim.getLocation(), item);
        }
    }
}
