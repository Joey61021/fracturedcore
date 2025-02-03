package com.fractured.enchants;

public final class GearType
{
    public static final int HELMET = 1 << 0;
    public static final int CHEST_PLATE = 1 << 1;
    public static final int LEGGINGS = 1 << 2;
    public static final int BOOTS = 1 << 3;
    public static final int ARMOR = HELMET | CHEST_PLATE | LEGGINGS | BOOTS;

    public static final int BOW = 1 << 4;
    public static final int CROSSBOW = 1 << 5;
    public static final int RANGED = BOW | CROSSBOW;

    public static final int SWORD = 1 << 6;
    public static final int TRIDENT = 1 << 7;

    public static final int SHOVEL = 1 << 8;
    public static final int AXE = 1 << 9;
    public static final int PICKAXE = 1 << 10;
    public static final int TOOLS = SHOVEL | AXE | PICKAXE;

    private GearType()
    {
    }
}
