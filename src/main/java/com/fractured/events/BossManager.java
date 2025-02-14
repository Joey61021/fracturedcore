package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
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

    @EventHandler
    public static void onTarget(EntityTargetEvent event)
    {
//        Entity is not a boss
        if (event.getEntity().getType() != EntityType.IRON_GOLEM || !event.getEntity().hasMetadata("boss"))
        {
            return;
        }

        if (!(event.getTarget() instanceof Player))
        {
            return;
        }
        event.setTarget(event.getTarget());
    }

    @EventHandler
    public static void onKill(EntityDeathEvent event)
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

        Bukkit.broadcastMessage(FracturedCore.getMessages().get(Messages.BOSS_KILLED));
        Bukkit.getOnlinePlayers().forEach(target -> target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F));
    }
}