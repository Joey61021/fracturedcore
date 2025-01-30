package com.fractured.team.upgrades;

import org.bukkit.Material;

public final class UpgradeCache
{
    private UpgradeCache()
    {

    }

    /**
     * Stores a list of all the upgrade details for each level of upgrading the team helmet
     */
    private static final UpgradeRequisite[] HELMET_UPGRADE_REQUIREMENTS = new UpgradeRequisite[6 /*MAX LEVEL*/];

    static
    {

        for (int i = 0; i < 6; ++i)
        {
            HELMET_UPGRADE_REQUIREMENTS[i].costType = Material.DIAMOND;
            HELMET_UPGRADE_REQUIREMENTS[i].cost = 5 + i;
        }
    }
}
