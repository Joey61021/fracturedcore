package com.fractured.team.upgrades;

import org.bukkit.Material;

public class UpgradeRequisite
{
    UpgradeRequisite next;
    // todo change from Material to something more specific
    Material costType;
    int level;
    int cost;

    public Material material()
    {
        return costType;
    }

    public int cost()
    {
        return cost;
    }
}
