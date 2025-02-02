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

            // if cause was from an entity
            if (event.getCause().name().toLowerCase().contains("entity")) {
                return;
            }
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

    private static void callDeath(EntityDamageEvent.DamageCause cause, Player victim, @Nullable Entity attacker)
    {
        String attackerName = "&7None";
        if (attacker != null) {
            attackerName = attacker instanceof Player ? ((Player) attacker).getDisplayName() : attacker.getName();
        }

        Bukkit.broadcastMessage(getDeathMessage(cause).replace("%victim%", victim.getDisplayName()).replace("%attacker%", attackerName));
    }

    private static String getDeathMessage(EntityDamageEvent.DamageCause cause)
    {
        return switch (cause) {
            case WORLD_BORDER -> FracturedCore.getMessages().get(Messages.DEATH_WORLD_BORDER);
            case CONTACT -> FracturedCore.getMessages().get(Messages.DEATH_CONTACT);
            case ENTITY_ATTACK -> FracturedCore.getMessages().get(Messages.DEATH_ENTITY_ATTACK);
            case ENTITY_SWEEP_ATTACK -> FracturedCore.getMessages().get(Messages.DEATH_ENTITY_SWEEP_ATTACK);
            case PROJECTILE -> FracturedCore.getMessages().get(Messages.DEATH_PROJECTILE);
            case SUFFOCATION -> FracturedCore.getMessages().get(Messages.DEATH_SUFFOCATION);
            case FALL -> FracturedCore.getMessages().get(Messages.DEATH_FALL);
            case FIRE, FIRE_TICK -> FracturedCore.getMessages().get(Messages.DEATH_FIRE);
            case MELTING -> FracturedCore.getMessages().get(Messages.DEATH_MELTING);
            case LAVA -> FracturedCore.getMessages().get(Messages.DEATH_LAVA);
            case DROWNING -> FracturedCore.getMessages().get(Messages.DEATH_DROWNED);
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> FracturedCore.getMessages().get(Messages.DEATH_BLOCK_EXPLOSION);
            case VOID -> FracturedCore.getMessages().get(Messages.DEATH_VOID);
            case LIGHTNING -> FracturedCore.getMessages().get(Messages.DEATH_LIGHTNING);
            case STARVATION -> FracturedCore.getMessages().get(Messages.DEATH_STARVATION);
            case POISON -> FracturedCore.getMessages().get(Messages.DEATH_POISON);
            case WITHER -> FracturedCore.getMessages().get(Messages.DEATH_WITHER);
            case FALLING_BLOCK -> FracturedCore.getMessages().get(Messages.DEATH_FALLING_BLOCK);
            case THORNS -> FracturedCore.getMessages().get(Messages.DEATH_THORNS);
            case DRAGON_BREATH -> FracturedCore.getMessages().get(Messages.DEATH_DRAGON_BREATH);
            case FLY_INTO_WALL -> FracturedCore.getMessages().get(Messages.DEATH_FLY_INTO_WALL);
            case HOT_FLOOR -> FracturedCore.getMessages().get(Messages.DEATH_HOT_FLOOR);
            case CRAMMING -> FracturedCore.getMessages().get(Messages.DEATH_CRAMMING);
            case DRYOUT -> FracturedCore.getMessages().get(Messages.DEATH_DRYOUT);
            case FREEZE -> FracturedCore.getMessages().get(Messages.DEATH_FREEZE);
            case SONIC_BOOM -> FracturedCore.getMessages().get(Messages.DEATH_SONIC_BOOM);
            case SUICIDE -> FracturedCore.getMessages().get(Messages.DEATH_SUICIDE);
            case MAGIC -> FracturedCore.getMessages().get(Messages.DEATH_MAGIC);
            default -> FracturedCore.getMessages().get(Messages.DEATH_KILL);
        };
    }
}
