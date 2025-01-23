package com.fractured.team;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team
{
    /**
     * As in the database
     */
    private final int teamId;
    private final String name;
    private final String color;
    private final Material item;
    private final Location spawn;
    private final List<Player> onlineTeamMembers;

    public Team(int teamId, String name, String color, Material item, Location spawn) {
        this.teamId = teamId;
        this.name = name;
        this.color = color;
        this.item = item;
        this.spawn = spawn;
        this.onlineTeamMembers = new ArrayList<>();
    }

    public boolean isOffline()
    {
        return onlineTeamMembers.isEmpty();
    }

    public void alert(String message)
    {
        for (Player player : onlineTeamMembers)
        {
            player.sendMessage(message);
        }
    }

    public String getName()
    {
        return name;
    }

    public String color()
    {
        return color;
    }

    public Material material()
    {
        return item;
    }

    public List<Player> getOnlineMembers()
    {
        return onlineTeamMembers;
    }

    public Location spawn()
    {
        return spawn;
    }
}
