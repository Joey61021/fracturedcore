package com.fractured.team;

import com.fractured.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class TeamCache
{
    private TeamCache()
    {

    }

    private static Inventory teamInventory;
    private static Map<Integer, Team> teams;
    private static Map<String, Team> teamsByName;

    public static void init(Map<Integer, Team> teams)
    {
        TeamCache.teams = teams;

        teamInventory = Bukkit.createInventory(null, 3 * 9, Utils.color("&7Select Team"));
        ItemStack item;
        ItemMeta meta;

        for (int i = 0; i < 4; ++i)
        {
            Team team = teams.get(i);
            item = new ItemStack(team.material());
            meta = item.getItemMeta();

            if (meta != null)
            {
                meta.setDisplayName(team.color() + team.getName() + " team");

                List<String> lore = new ArrayList<>();
                lore.add(Utils.color("&7Online: " + team.getOnlineMembers().size()));

                meta.setLore(lore);

                item.setItemMeta(meta);
            }

            teamInventory.setItem(9 + 1, item);
        }
    }

    public static Collection<Team> teams()
    {
        return teams.values();
    }

    public static Team getTeam(int id)
    {
        return teams.get(id);
    }

    public static void openMenu(Player player)
    {
        player.openInventory(teamInventory);
    }
}
