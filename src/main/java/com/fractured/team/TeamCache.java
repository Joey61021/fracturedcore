package com.fractured.team;

import com.fractured.FracturedCore;
import com.fractured.menu.Menu;
import com.fractured.menu.MenuManager;
import com.fractured.util.globals.ConfigKeys;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class TeamCache
{
    private TeamCache()
    {

    }

    private static Inventory teamInventory;
    private static Map<Integer, Team> teamsById;
    /**
     * Used for commands like /team set [username] [name]
     */
    private static Map<String, Team> teamsByName;
    private static final String SELECT_TEAM = "Select Team";

    private static Team GREEN;
    private static Team RED;
    private static Team BLUE;
    private static Team YELLOW;

    private static void addTeam(HumanEntity player, Team team)
    {
        TeamManager.addTeam((Player) player, team);

        player.closeInventory();
    }

    private static ItemStack getItemForTeam(Material material, Team team)
    {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setLore(List.of(ChatColor.GRAY + "Online: (" + team.getOnlineMembers().size() + "/" + team.getTotalMembers() + ")"));

        if (FracturedCore.getFracturedConfig().get(ConfigKeys.MEMBER_CAP_ENABLED))
        {
            int members = team.getTotalMembers();
            final int maxGap = FracturedCore.getFracturedConfig().get(ConfigKeys.MAX_PLAYER_GAP);

            // team is getting compared against itself here, I know
            if (members - GREEN.getTotalMembers() > maxGap ||
                members - RED.getTotalMembers() > maxGap ||
                members - BLUE.getTotalMembers() > maxGap ||
                members - YELLOW.getTotalMembers() > maxGap)
            {
                /* If the number of members in this team exceeds the number of
                   members in any other team by maxGap, we lock it down. */
                meta.setDisplayName(ChatColor.RED + "(LOCKED) " + team.color() + team.getName() + " Team");

                item.setItemMeta(meta);
                return item;
            }
        }

        meta.setDisplayName(team.color() + team.getName() + " Team");

        item.setItemMeta(meta);
        return item;
    }

    public static void updateTeamInventory()
    {
        teamInventory.setItem(10, getItemForTeam(Material.GREEN_CONCRETE, GREEN));
        teamInventory.setItem(12, getItemForTeam(Material.RED_CONCRETE, RED));
        teamInventory.setItem(14, getItemForTeam(Material.BLUE_CONCRETE, BLUE));
        teamInventory.setItem(16, getItemForTeam(Material.YELLOW_CONCRETE, YELLOW));
    }

    public static void init(Map<Integer, Team> teamsIdMap)
    {
        teamsById = teamsIdMap;
        teamsByName = new HashMap<>();

        for (Team team : teamsById.values())
        {
            teamsByName.put(team.getName().toLowerCase(), team);
        }

        GREEN = getTeam("green");
        RED = getTeam("red");
        BLUE = getTeam("blue");
        YELLOW = getTeam("yellow");

        // init team inventory (and teamsByName)
        teamInventory = Bukkit.createInventory(null, 9 * 3, SELECT_TEAM);

        Menu selectTeam = new Menu();

        // 0   1   2   3   4   5   6   7   8
        // 9  *10  11 *12  13 *14  15 *16  17
        // 18  19  20  21  22  23  24  25  26
        selectTeam.register(10, event -> addTeam(event.getView().getPlayer(), GREEN));
        selectTeam.register(12, event -> addTeam(event.getView().getPlayer(), RED));
        selectTeam.register(14, event -> addTeam(event.getView().getPlayer(), BLUE));
        selectTeam.register(16, event -> addTeam(event.getView().getPlayer(), YELLOW));

        MenuManager.register(SELECT_TEAM, selectTeam);

        // wait until all four teams are put into the HashMap
        updateTeamInventory();
    }

    public static Collection<Team> teams()
    {
        return teamsById.values();
    }

    public static Team getTeam(int id)
    {
        return teamsById.get(id);
    }

    public static Team getTeam(String name)
    {
        return teamsByName.get(name.toLowerCase());
    }

    /**
     * Tries to get the requested team by name or id
     *
     * @param s some identifier that (should) connects to a team
     * @return Doesn't guarentee that the team is not null.
     */
    public static Team getTeamByPhrase(String s)
    {
        // Get by name first
        Team rax = getTeam(s);

        // If there is no team by that name, try the id
        if (rax == null)
        {
            int i;

            try
            {
                i = Integer.parseInt(s);
            } catch (NumberFormatException e)
            {
                // id is invalid, so no team could be found
                return null;
            }

            // Input is an int, we can try by the team's id
            rax = TeamCache.getTeam(i);
        }

        return rax;
    }

    public static void openMenu(Player player)
    {
        updateTeamInventory();
        player.openInventory(teamInventory);
    }
}
