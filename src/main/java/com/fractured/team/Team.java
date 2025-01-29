package com.fractured.team;

import com.fractured.menu.MenuManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Team
{
    /**
     * As in the database
     */
    private final int teamId;
    private int totalMembers;
    private final String name;
    private final String color;
    private final Material item;
    private final Material beacon;
    private final Location spawn;
    private final List<Player> onlineTeamMembers;
    private ItemStack helmet;
    private final Inventory upgradesMenu;

    public Team(int teamId, int totalMembers, String name, String color, Material item, Material beacon, Location spawn) {
        this.teamId = teamId;
        this.totalMembers = totalMembers;
        this.name = name;
        this.color = color;
        this.item = item;
        this.beacon = beacon;
        this.spawn = spawn;
        this.onlineTeamMembers = new ArrayList<>();
        this.upgradesMenu = MenuManager.register(9 * 3, "Team Upgrades", event -> {
            if (event.getSlot() == 9 + 3)
            {

            }
        });
    }

    public void alert(String message)
    {
        for (Player player : onlineTeamMembers)
        {
            player.sendMessage(message);
        }
    }

    public boolean isOffline()
    {
        return onlineTeamMembers.isEmpty();
    }

    /**
     * Total members, online or offline.
     */
    public int getTotalMembers()
    {
        return totalMembers;
    }

    public int getId()
    {
        return teamId;
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

    /**
     * @return Beacon material
     */
    public Material beacon()
    {
        return beacon;
    }

    public List<Player> getOnlineMembers()
    {
        return onlineTeamMembers;
    }

    public Location spawn()
    {
        return spawn;
    }

    public ItemStack getHelmet()
    {
        return helmet;
    }

    public Inventory getUpgradesMenu()
    {
        return upgradesMenu;
    }
}
