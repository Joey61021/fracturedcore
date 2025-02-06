package com.fractured.shields;

import com.fractured.util.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public class Shield {

    private Block block;
    private int radius;
    private ArmorStand armorStand;
    private ShieldState state;

    public Shield(Block block, int radius, ArmorStand armorStand) {
        this.block = block;
        this.radius = radius;
        this.armorStand = armorStand;
        this.state = ShieldState.AWAITING_CHANGE;
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
}
