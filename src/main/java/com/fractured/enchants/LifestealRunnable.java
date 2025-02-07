package com.fractured.enchants;

import com.fractured.FracturedCore;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LifestealRunnable extends BukkitRunnable
{
    // fixme server shutting down / crashing / logging out
    private static final Map<UUID, Map<UUID, LifestealRunnable>> lifesteals = new HashMap<>();

    public static LifestealRunnable get(LivingEntity damager, LivingEntity target)
    {
        Map<UUID, LifestealRunnable> targets = lifesteals.get(damager.getUniqueId());

        if (targets != null)
        {
            return targets.get(target.getUniqueId());
        }
        return null;
    }

    public static void put(LifestealRunnable runnable)
    {
        Map<UUID, LifestealRunnable> targets = lifesteals.computeIfAbsent(runnable.damager.getUniqueId(), k -> new HashMap<>());

        targets.put(runnable.target.getUniqueId(), runnable);
    }

    private final LivingEntity target;
    private final LivingEntity damager;

    private double healthBorrowed = 0;

    public LifestealRunnable(LivingEntity target, LivingEntity damager)
    {
        this.target = target;
        this.damager = damager;
    }

    public void start()
    {
        FracturedCore.runDelay(this, 3 * 20);
    }

    public void restart()
    {
        cancel();
        start();
    }

    /**
     * Borrows a certain amount of health from the target, and adds it to the damager.
     * The object keeps track of the total amount of cummulative health borrowed.
     */
    public void borrowHealth(double healthBorrowed)
    {
        this.healthBorrowed += healthBorrowed;

        // Borrow health
        target.setMaxHealth(target.getMaxHealth() - healthBorrowed);
        // Add health to damager
        damager.setMaxHealth(damager.getMaxHealth() + healthBorrowed);
    }

    // lifesteal expires
    @Override
    public void run()
    {
        // reset
        borrowHealth(-healthBorrowed);

        Map<UUID, LifestealRunnable> targets = lifesteals.get(damager.getUniqueId());

        if (targets.size() == 1)
        {
            lifesteals.remove(damager.getUniqueId());
        }
    }
}
