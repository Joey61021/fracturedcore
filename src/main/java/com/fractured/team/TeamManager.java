package com.fractured.team;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Map;

public class TeamManager
{
    public static ItemStack getHelmet(Team team)
    {
//        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
//        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
//
//        Map<ChatColor, Color> colorMap = Map.of(
//                ChatColor.RED, Color.RED,
//                ChatColor.BLUE, Color.BLUE,
//                ChatColor.GREEN, Color.GREEN
//        );
//
//        ChatColor color = team.getColor();
//        meta.setColor(colorMap.getOrDefault(color, Color.YELLOW));
//
//        meta.addEnchant(Enchantment.PROTECTION, FracturedCore.getSettings.getInt("upgrades." + team.getName().toLowerCase() + "." + Upgrades.HELMET_PROTECTION.getUpgradeValue(), 1), true);
//        meta.setUnbreakable(true);
//        meta.setDisplayName(Utils.color(team.color() + team.getName() + " team"));
//        item.setItemMeta(meta);
        return null; // return item
    }

    public static void toggleTeamChat(Player player)
    {
        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.CMD_TC_NOT_IN_TEAM));
        } else if (user.isInTeamChat())
        {
            user.setInTeamChat(false);
            player.sendMessage(FracturedCore.getMessages().get(Messages.CMD_TC_TOGGLE_OFF));
        } else
        {
            user.setInTeamChat(true);
            player.sendMessage(FracturedCore.getMessages().get(Messages.CMD_TC_TOGGLE_ON));
        }
    }

    /**
     * Removes a player from a team.
     * <p>
     * In total, this makes a database call, sends the player a message, and sets
     * the team in their corresponding {@link User} object. This also updates
     * the player's tab list and display name.
     * @apiNote The database call is asynchronous
     */
    public static void removeTeam(Player player, User user)
    {
        player.setDisplayName(ChatColor.GRAY + player.getName());

        player.setPlayerListName(player.getDisplayName());
        player.setPlayerListFooter(Messages.NO_TEAM_TAB_LIST_FOOTER);

        FracturedCore.runAsync(() ->
        {
            FracturedCore.getStorage().removeTeam(user);
        });

        if (user.getTeam() != null)
        {
            // Removing them from a team
            player.sendMessage("You have been removed from " + user.getTeam().getName() + " team.");

            // This goes last because preprocessing requires the user's last team before it be changed.
            user.setTeam(null);
        }
    }

    /**
     * Adds a player from a team.
     * <p>
     * In total, this makes a database call, sends the player a message, and sets
     * the team in their corresponding {@link User} object. This also updates
     * the player's tab list and display name.
     * @apiNote The database call is asynchronous
     */
    public static void addTeam(Player player, User user, Team team)
    {
        // Display name
        player.setDisplayName(team.color() + player.getName());

        // Tab List
        player.setPlayerListName(player.getDisplayName());
        player.setPlayerListFooter(Utils.color("&7Your team: " + team.color() + team.getName() + " team"));

        // Database
        FracturedCore.runAsync(() ->
        {
            FracturedCore.getStorage().assignTeam(user, team);
        });

        // User Object
        // This goes last because preprocessing requires the user's last team before it be changed.
        user.setTeam(team);

        // Messages
        player.sendMessage("Joined " + team.getName() + " team...");
    }

    public static void removeTeam(Player player)
    {
        removeTeam(player, UserManager.getUser(player.getUniqueId()));
    }

    public static void addTeam(Player player, Team team)
    {
        addTeam(player, UserManager.getUser(player.getUniqueId()), team);
    }

    /**
     * Removes the player from their current team and adds them to the new one, no matter what.
     */
    public static void forceSetTeam(Player player, User user, Team team)
    {
        if (user.getTeam() != null)
        {
            removeTeam(player, user);
        }

        // If the team passed is null, we don't have to do anything extra, since we'd
        // just be removing their team of the player, and that's already done.

        if (team != null)
        {
            addTeam(player, user, team);
        }
    }
}
