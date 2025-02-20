
package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.ConfigKeys;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HealthListener implements Listener
{

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        if (!(victim instanceof LivingEntity))
        {
            return;
        }

        if (!(victim instanceof Player))
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(FracturedCore.class), () -> updateName((LivingEntity) victim), 1);
            return;
        }

        // if friendly fire is off (we should cancel the event if the same team is attacking itself)
        if (!FracturedCore.getFracturedConfig().get(ConfigKeys.FRIENDLY_FIRE))
        {
            User damagerUser = UserManager.getUser(damager.getUniqueId());

            // Because these are being taken from an entity, we have to check if it's null or not. The entity is a player if there is a user corresponding to that uuid
            if (damagerUser == null)
            {
                return;
            }

            User victimUser = UserManager.getUser(victim.getUniqueId());

            if (victimUser == null)
            {
                return;
            }
            Team damagerTeam = damagerUser.getTeam();

            if (victimUser.getTeam() == damagerTeam)
            {
                event.setCancelled(true);
                damager.sendMessage(FracturedCore.getMessages().get(Messages.FRIENDLY_FIRE_DISABLED));
            }
        }

        // AXE OF PERUN ENCHANT LOGIC
        // fixme

        if (!(damager instanceof Player))
        {
            return;
        }

        ItemStack item = ((Player) damager).getItemInUse();

        if (!damager.hasPermission(Permissions.COMMAND_WORLD_ADMIN) || item == null || item.getType().equals(Material.AIR) || item.getItemMeta() == null || !item.getItemMeta().hasEnchants())
        {
            return;
        }

        if (item.getType().equals(Material.IRON_AXE) && item.getItemMeta().hasEnchant(Enchantment.UNBREAKING))
        {
            // todo fixme - chance is dependant on level, so for this instance level is 2, so 2*20
            double chance = 2 * 20;
            if (Math.random() * 100 <= chance)
            {
                victim.getWorld().strikeLightning(victim.getLocation());
            }
        }
    }

    @EventHandler
    public static void onHeal(EntityRegainHealthEvent event)
    {
        if (!(event.getEntity() instanceof LivingEntity) || !event.getEntity().isCustomNameVisible())
        {
            return;
        }

        updateName((LivingEntity) event.getEntity());
    }

    public static void updateName(LivingEntity entity)
    {
        if (entity instanceof ArmorStand)
        {
            return;
        }

        // Remove final damage from health, if any
        double health = entity.getHealth() / entity.getMaxHealth() * 100;
        entity.setCustomNameVisible(true);
        entity.setCustomName(Utils.color((health < 25 ? "&c" : health < 50 ? "&e" : "&a") + Math.round(health) + "%"));
    }
}
