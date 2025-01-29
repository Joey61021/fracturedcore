package com.fractured.team;

import org.bukkit.Location;

public class Claim
{
    /**
     * The team that this claim belongs to
     */
    private final Team team;
    /**
     * Minimum x component
     */
    private int x0;
    /**
     * Minimum y component
     */
    private int z0;
    /**
     * Maximum z component
     */
    private int x1;
    /**
     * Maximum z component
     */
    private int z1;

    public Team getTeam()
    {
        return team;
    }

    public int getX0()
    {
        return x0;
    }

    public int getZ0()
    {
        return z0;
    }

    public int getX1()
    {
        return x1;
    }

    public int getZ1()
    {
        return z1;
    }

    protected Claim(Team team, int x0, int z0, int x1, int z1)
    {
        this.team = team;

        this.x0 = Math.min(x0, x1);
        this.z0 = Math.min(z0, z1);
        this.x1 = Math.max(x0, x1);
        this.z1 = Math.max(z0, z1);
    }

    public boolean contains(int x, int z)
    {
        return x0 <= x && x <= x1 && z0 <= z && z <= z1;
    }

    public boolean contains(Location loc)
    {
        return contains(loc.getBlockX(), loc.getBlockZ());
    }
}