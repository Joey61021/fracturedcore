package com.fractured.team;

import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ClaimManager implements Listener
{
    private static final List<TeamClaim> claims = new ArrayList<>();

    public static void addClaim(TeamClaim claim)
    {
        claims.add(claim);
    }

    public static TeamClaim getClaim(int x, int z)
    {
        for (TeamClaim claim : claims)
        {
            if (claim.contains(x, z))
            {
                return claim;
            }
        }
        return null;
    }
}
