package com.fractured.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class DungeonBoss extends IronGolem
{
    public DungeonBoss(Location loc)
    {
        super(EntityType.IRON_GOLEM, ((CraftWorld) loc.getWorld()).getHandle());
        setPos(loc.getX(), loc.getY(), loc.getZ());

        goalSelector.removeAllGoals(g -> true);
        targetSelector.removeAllGoals(g -> true);

        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new MeleeAttackGoal(this, 2.0D, true));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
