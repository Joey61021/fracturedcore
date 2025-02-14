package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public class BossManager implements Listener {

    public static int MAX_HEALTH = 200;

    public static void spawnBoss(Location location)
    {
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.IRON_GOLEM);

        entity.setCustomNameVisible(true);
        entity.setCustomName(Utils.color("&cTemple Guardian"));
        entity.setMaxHealth(MAX_HEALTH);
        entity.setHealth(MAX_HEALTH);
        entity.setRemoveWhenFarAway(false);

//        Potion effects, just in case you couldn't read
        Set<PotionEffect> effects = new HashSet<>();
        effects.add(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        effects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        effects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 2));
        effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
        entity.addPotionEffects(effects);

        entity.setMetadata("boss", new FixedMetadataValue(JavaPlugin.getPlugin(FracturedCore.class), true));

        Bukkit.broadcastMessage(FracturedCore.getMessages().get(Messages.BOSS_SPAWNED));
        Bukkit.getOnlinePlayers().forEach(target -> target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F));
    }

    public static Player getNearestTarget(Location location)
    {
        Player nearestTarget = null;
        double closestDistance = Double.MAX_VALUE;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().equals(location.getWorld()))
            {
                continue; // Skip if in a different world
            }

            double distance = player.getLocation().distance(location);
            if (distance < 40 && distance < closestDistance)
            {
                closestDistance = distance;
                nearestTarget = player;
            }
        }

        return nearestTarget;
    }

    @EventHandler
    public static void onDeath(EntityDeathEvent event)
    {
        //        Entity is not a boss
        if (event.getEntity().getType() != EntityType.IRON_GOLEM || !event.getEntity().hasMetadata("boss"))
        {
            return;
        }

//        TODO -- loot.yml system
        Set<ItemStack> items = new HashSet<>();
        items.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
        items.add(new ItemStack(Material.NETHER_STAR, 1));
        items.add(new ItemStack(Material.NETHERITE_INGOT, 3));
        items.add(new ItemStack(Material.DIAMOND, 3));
        event.getDrops().addAll(items);
    }

    @EventHandler
    public static void onDeath(EntityDamageByEntityEvent event)
    {
//        Entity is not a boss
        if (!(event.getDamager() instanceof Player) || event.getEntity().getType() != EntityType.IRON_GOLEM || !event.getEntity().hasMetadata("boss"))
        {
            return;
        }

        Player damager = (Player) event.getDamager();
        IronGolem victim = (IronGolem) event.getEntity();

//        Entity is not dead
        if (victim.getHealth() - event.getFinalDamage() > 0)
        {
            return;
        }

        Bukkit.broadcastMessage(FracturedCore.getMessages().get(Messages.BOSS_KILLED).replace("%killer%", damager.getDisplayName()));
        Bukkit.getOnlinePlayers().forEach(target -> target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F));
    }
}