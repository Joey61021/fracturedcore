package com.fractured.team;

import com.fractured.menu.InventoryCallback;
import com.fractured.menu.ItemBuilder;
import com.fractured.menu.Menu;
import com.fractured.menu.MenuManager;
import com.fractured.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
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

    private static final Team GREEN = getTeam("green");
    private static final Team RED = getTeam("red");
    private static final Team BLUE = getTeam("blue");
    private static final Team YELLOW = getTeam("yellow");

    private static void addTeam(HumanEntity player, Team team)
    {
        TeamManager.addTeam((Player) player, team);

        player.closeInventory();
    }

    private static ItemStack getItemForTeam(Material material, Team team)
    {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(team.color() + team.getName() + " Team");
        meta.setLore(List.of("Online: (" + team.getOnlineMembers() + "/" + team.getTotalMembers() + ")"));

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

        // init team inventory (and teamsByName)
        teamInventory = Bukkit.createInventory(null, 9 * 3, SELECT_TEAM);

        for (Team team : teamsById.values())
        {
            teamsByName.put(team.getName().toLowerCase(), team);
        }

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

    public static void openMenu(Player player)
    {
        updateTeamInventory();
        player.openInventory(teamInventory);
    }
}
