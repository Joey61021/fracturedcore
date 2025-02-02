package com.fractured.storage;

import com.fractured.team.Team;
import com.fractured.team.TeamCache;
import com.fractured.team.Claim;
import com.fractured.user.User;
import org.bukkit.*;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class TestStorage implements Storage
{
    private final World world = Bukkit.getWorld("world");
    private final Team green = new Team(1, 2, "Green", 'a', new Location(world, 0, 200, 0));

    @Override
    public void saveClaim(World world, Claim claim)
    {

    }

    @Override
    public void initServerResources()
    {
        Bukkit.getLogger().info("Initializing server resources");

        Map<Integer, Team> teams = new HashMap<>();

        Team red = new Team(2, 3, "Red", 'c', new Location(world, 0, 200, 0));
        Team blue = new Team(3, 1, "Blue", '9', new Location(world, 0, 200, 0));
        Team yellow = new Team(4, 4, "Yellow", 'e', new Location(world, 0, 200, 0));

        teams.put(1, green);
        teams.put(2, red);
        teams.put(3, blue);
        teams.put(4, yellow);

        Bukkit.getLogger().info(Integer.toString(teams.size()));
        TeamCache.init(teams);

//        ClaimManager.addClaim(new Claim(red, -1, 1, -1000, 1000));
//        ClaimManager.addClaim(new Claim(yellow, -1, -1, -1000, -1000));
//        ClaimManager.addClaim(new Claim(green, 1, 1, 1000, 1000));
//        ClaimManager.addClaim(new Claim(blue, 1, -1, 1000, -1000));
    }

    @Override
    public void setClaims(Claim yellow, Claim green, Claim red, Claim blue)
    {

    }

    @Override
    public void setTeam(CommandSender staff, String reason, User user, Team team)
    {
        Bukkit.getLogger().info("Assigning team to user " + user.getId());
    }

    @Override
    public void removeTeam(CommandSender staff, String reason, User user, Team team)
    {
        Bukkit.getLogger().info("Removing team from user " + user.getId());
    }

    @Override
    public void load(User user)
    {
        Bukkit.getLogger().info("Loading user " + user.getId());
        user.setTeam(green);
    }

    @Override
    public void save(User user)
    {
        Bukkit.getLogger().info("Saving user " + user.getId());
    }
}
