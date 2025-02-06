package com.fractured.team;

import com.fractured.FracturedCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public final class ClaimManager
{
    private ClaimManager()
    {}

    // Each world gets its own ClaimManager. This allows for claiming in multiple worlds.
    private static final Map<UUID, ClaimManager> worlds = new HashMap<>();

    static
    {
        // fixme only worlds on startup can have claims
        for (World world : Bukkit.getWorlds())
        {
            worlds.put(world.getUID(), new ClaimManager());
        }
    }

    /**
     * Creates a new claim and registers it to the claim manager. The claim that is created
     * here is, and cannot be saved in the database. For that, refer to
     * {@link ClaimManager#newStoredClaim(World, Team, int, int, int, int)}
     */
    public static Claim newClaim(World world, Team team, int x0, int z0, int x1, int z1, boolean shield)
    {
        if (team == null)
        {
            throw new IllegalArgumentException("team cannot be null");
        }

        Claim rax = new Claim(team, x0, z0, x1, z1, shield);

        worlds.get(world.getUID()).addClaim(rax);

        return rax;
    }

    public static Claim newStoredClaim(World world, Team team, int x0, int z0, int x1, int z1, boolean shield)
    {
        Claim claim = newClaim(world, team, x0, z0, x1, z1, shield);

        FracturedCore.getStorage().saveClaim(world, claim);

        return claim;
    }

    /**
     * Gets the claim at the location, taking into account the world of the location
     */
    public static Claim getClaim(Location loc)
    {
        World world = loc.getWorld();

        if (world != null)
        {
            ClaimManager manager = worlds.get(world.getUID());

            return manager.getClaim(loc.getBlockX(), loc.getBlockZ());
        }
        return null;
    }

    private final List<Claim> claims = new ArrayList<>();

    public void addClaim(Claim claim)
    {
        claims.add(claim);
    }

    public Claim getClaim(int x, int z) {
        Claim fetched = null;

        for (Claim claim : claims) {
            if (claim.contains(x, z)) {
                if (claim.getShield()) {
                    return claim;
                }

                if (fetched == null) {
                    fetched = claim;
                }
            }
        }
        return fetched;
    }
}
