package com.fractured.events;

import com.fractured.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Location loc = victim.getLocation();

        victim.sendMessage(Utils.color("&7You died at (" + Math.round(loc.getX()) + ", " + Math.round(loc.getY()) + ", " + Math.round(loc.getZ()) + ")"));

        // fixme, on damage event can we set their helmet to null so we don't have to do this slow loop?
        for (ItemStack item : event.getDrops()) {
            if (item != null && item.getType().equals(Material.LEATHER_HELMET) && item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                item.setType(Material.AIR);
            }
        }

        if (victim.getKiller() != null) {
            event.setDeathMessage(Utils.color(victim.getDisplayName() + " &fwas killed by " + victim.getKiller().getDisplayName()));
        }
        // todo custom death messages
    }
}
