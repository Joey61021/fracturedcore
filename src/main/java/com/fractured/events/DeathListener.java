package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nullable;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Location loc = victim.getLocation();

        victim.sendMessage(Utils.color("&7You died at (" + Math.round(loc.getX()) + ", " + Math.round(loc.getY()) + ", " + Math.round(loc.getZ()) + ")"));

        event.setDeathMessage("");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player victim))
        {
            return;
        }

        if (victim.getHealth() - event.getFinalDamage() <= 0)
        {
            victim.getInventory().setHelmet(null);
            callDeath(event.getCause(), victim, null);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim))
        {
            return;
        }

        if (victim.getHealth() - event.getFinalDamage() <= 0)
        {
            victim.getInventory().setHelmet(null);
            callDeath(event.getCause(), victim, victim.getKiller());
        }
    }

    private static void callDeath(EntityDamageEvent.DamageCause cause, Player victim, @Nullable Entity target)
    {
        Bukkit.broadcastMessage(getDeathMessage(cause));
    }

    private static String getDeathMessage(EntityDamageEvent.DamageCause cause)
    {
        return switch (cause) {
            case THORNS -> FracturedCore.getMessages().get(Messages.DEATH_CACTUS);
            case DROWNING -> FracturedCore.getMessages().get(Messages.DEATH_DROWNED);
            case FLY_INTO_WALL -> FracturedCore.getMessages().get(Messages.DEATH_ELYTRA);
            case ENTITY_EXPLOSION, BLOCK_EXPLOSION -> FracturedCore.getMessages().get(Messages.DEATH_EXPLOSION);
            case FALL -> FracturedCore.getMessages().get(Messages.DEATH_FALLING);
            case FALLING_BLOCK -> FracturedCore.getMessages().get(Messages.DEATH_BLOCKS);
            case FIRE, FIRE_TICK -> FracturedCore.getMessages().get(Messages.DEATH_FIRE);
            case LAVA -> FracturedCore.getMessages().get(Messages.DEATH_LAVA);
            case LIGHTNING -> FracturedCore.getMessages().get(Messages.DEATH_LIGHTNING);
            case HOT_FLOOR -> FracturedCore.getMessages().get(Messages.DEATH_MAGMA_BLOCK);
            case MAGIC -> FracturedCore.getMessages().get(Messages.DEATH_MAGIC);
            case FREEZE -> FracturedCore.getMessages().get(Messages.DEATH_POWDERED_SNOW);
            case ENTITY_ATTACK -> FracturedCore.getMessages().get(Messages.DEATH_PLAYER_MOB);
            case SONIC_BOOM -> FracturedCore.getMessages().get(Messages.DEATH_WARDEN_SONIC_CHARGE);
            case PROJECTILE -> FracturedCore.getMessages().get(Messages.DEATH_PLAYER_SHOT);
            default -> "You died from unknown causes.";
        };
    }
}
