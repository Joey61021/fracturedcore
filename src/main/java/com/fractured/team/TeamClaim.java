package com.fractured.team;

public class TeamClaim
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

    public TeamClaim(Team team, int x0, int z0, int x1, int z1)
    {
        this.team = team;

        this.x0 = Math.min(x0, x1);
        this.z0 = Math.min(z0, z1);
        this.x1 = Math.max(x0, x1);
        this.z1 = Math.max(z0, z1);
    }

    public Team getTeam()
    {
        return team;
    }

    public boolean contains(int x, int z)
    {
        return x0 <= x && x <= x1 && z0 <= z && z <= z1;
    }
}
