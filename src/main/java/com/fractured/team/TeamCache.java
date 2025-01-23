package com.fractured.team;

import com.fractured.util.Utils;
import org.bukkit.Bukkit;
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

    public static void init(Map<Integer, Team> teamsIdMap)
    {
        TeamCache.teamsById = teamsIdMap;
        teamsByName = new HashMap<>();

        // init team inventory (and teamsByName)
        teamInventory = Bukkit.createInventory(null, 3 * 9, Utils.color("&7Select Team"));
        int i = 0;
        ItemStack item;
        ItemMeta meta;

        // todo Right now we only need to consider the first four teams
        // This could be changed if we wanted to add more, but there's
        // no way to add teams right now, so there's no point in porting
        // this.
        for (Team team : teamsIdMap.values())
        {
            // fixme This probably isn't the best way to do this, I
            // just did it like this because it seemed the simplest

            // Add the teamsByName mapping START

            teamsByName.put(team.getName().toLowerCase(), team);

            // END

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

            teamInventory.setItem(9 + 1 + 2 * (i++), item);
        }
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
        player.openInventory(teamInventory);
    }
}
