package com.fractured.shields;

import com.fractured.team.Team;
import com.fractured.util.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public class Shield {

    private Block block;
    private int radius;
    private ArmorStand armorStand;
    private Team team;
    private ShieldState state;

    public Shield(Block block, int radius, ArmorStand armorStand, Team team) {
        this.block = block;
        this.radius = radius;
        this.armorStand = armorStand;
        this.team = team;

        this.state = ShieldState.EDITING;
    }

    public Block getBlock() {
        return block;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setHologram(String text) {
        if (armorStand != null) {
            armorStand.setCustomName(Utils.color(text));
        }
    }

    public void setState(ShieldState state)
    {
        this.state = state;
        setHologram(state.getName());
    }

    public ShieldState getState() {
        return state;
    }

    public Team getTeam()
    {
        return team;
    }
}
