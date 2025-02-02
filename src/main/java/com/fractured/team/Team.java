package com.fractured.team;

import com.fractured.menu.Menu;
import com.fractured.menu.MenuManager;
import com.fractured.team.upgrades.Upgrades;
import com.fractured.team.upgrades.UpgradeRequisite;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team
{
    /**
     * As in the database
     */
    private final int teamId;
    private int totalMembers;
    private final String name;
    private final ChatColor color;
    private final Material beacon;
    private final Location spawn;
    private final List<Player> onlineTeamMembers;
    private ItemStack helmet;
    private final Map<Integer, UpgradeRequisite> upgrades;
    private final Inventory upgradesMenu;

    private static final String TEAM_UPGRADES = "Team Upgrades";

    static
    {
        Menu teamUpgrades = new Menu();
        teamUpgrades.register(9 + 4, event ->
        {
            HumanEntity player = event.getView().getPlayer();
            User user = UserManager.getUser(player);
            Team team = user.getTeam();

            if (team == null)
            {
                player.sendMessage("You are not in a team!");
                player.closeInventory();
            } else
            {
                UpgradeRequisite requisite = team.upgrades.get(Upgrades.HELMET_ID);

                Material costType = requisite.material();
                int cost = requisite.cost();

                player.sendMessage("TODO");
            }

            // upgrades
        });

        MenuManager.register(TEAM_UPGRADES, teamUpgrades);
    }

    public Team(int teamId, int totalMembers, String name, char color, Location spawn)
    {
        // Stuff from the database
        this.teamId = teamId;
        this.totalMembers = totalMembers;
        this.name = name;
        this.color = ChatColor.getByChar(color);
        this.spawn = spawn;

        beacon = Utils.getGlassFrom(color);

        this.onlineTeamMembers = new ArrayList<>();
        this.upgrades = new HashMap<>();

        this.upgradesMenu = Bukkit.createInventory(null, 3 * 9, TEAM_UPGRADES);
    }

    public void upgrade(Upgrades upgrade)
    {
        // When an upgrade is submitted to a team,
        // * set the level in the team upgrade's map
        // * call the upgrade callback
        // * update how to get the cost
        //upgrades.get
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

    public ChatColor color()
    {
        return color;
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
