package com.fractured.team;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.ConfigKeys;
import com.fractured.util.globals.Messages;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeamManager
{
    public static void toggleTeamChat(Player player)
    {
        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_TEAM_BLOCKED));
        } else if (user.isInTeamChat())
        {
            user.setInTeamChat(false);
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_CHAT_TOGGLE_OFF));
        } else
        {
            user.setInTeamChat(true);
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_CHAT_TOGGLE_ON));
        }
    }

    // Called if the user is known NOT to be in a team. This skips those checks
    private static void addTeamCleared(CommandSender staff, String reason, Player player, User user, Team team)
    {
        // Database
        FracturedCore.runAsync(() ->
        {
            FracturedCore.getStorage().setTeam(staff, reason, user, team);
        });

        // User Object
        // This goes last because preprocessing requires the user's last team before it be changed.
        user.setTeam(team);
        team.addMember(player);
        player.teleport(team.spawn());

        if (FracturedCore.getFracturedConfig().get(ConfigKeys.STARTER_ITEMS))
        {
            player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
            player.getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
            player.getInventory().addItem(new ItemStack(Material.WOODEN_PICKAXE));
        }
    }

    /**
     * @param phrase Team
     * @return 0 for success, 1 for already in team, 2 for invalid team
     */
    public static int addTeam(CommandSender staff, String reason, Player player, User user, String phrase)
    {
        if (user.getTeam() != null)
        {
            return 1; // user is already in a team
        }

        Team team = TeamCache.getTeamByPhrase(phrase);

        if (team == null)
        {
            return 2; // invalid team
        }

        addTeamCleared(staff, reason, player, user, team);
        return 0; // success
    }

    /**
     * Adds a player to a team.
     * <p>
     * In total, this makes a database call, sends the player a message, and sets
     * the team in their corresponding {@link User} object. This also updates
     * the player's tab list and display name.
     * @apiNote The database call is asynchronous
     * @return True if the user was added to the team. False if they were already in a team.
     */
    public static boolean addTeam(CommandSender staff, String reason, Player player, User user, Team team)
    {
        if (user.getTeam() != null)
        {
            return false;
        }

        addTeamCleared(staff, reason, player, user, team);
        return true;
    }

    public static boolean addTeam(CommandSender staff, String reason, Player player, Team team)
    {
        return addTeam(staff, reason, player, UserManager.getUser(player.getUniqueId()), team);
    }

    public static boolean addTeam(Player player, Team team)
    {
        return addTeam(null, null, player, team);
    }

    // Called if the player is known to have a team. This method skips those checks
    private static void removeTeamCleared(CommandSender staff, String reason, Player player, User user)
    {
        // Store the value so it's not cleared in the async call (stupid)
        Team team = user.getTeam();

        FracturedCore.runAsync(() ->
        {
            FracturedCore.getStorage().removeTeam(staff, reason, user, team);
        });

        // Removing them from a team
        user.getTeam().removeMember(player);
        // This goes last because preprocessing requires the user's last team before it be changed.
        user.setTeam(null);
    }

    /**
     * Removes a player from a team.
     * <p>
     * In total, this makes a database call, sends the player a message, and sets
     * the team in their corresponding {@link User} object. This also updates
     * the player's tab list and display name.
     * @apiNote The database call is asynchronous
     * @return True if the user's team was removed. False if they weren't in a team
     */
    public static boolean removeTeam(CommandSender staff, String reason, Player player, User user)
    {
        if (user.getTeam() == null)
        {
            return false;
        }

        removeTeamCleared(staff, reason, player, user);
        return true;
    }

    public static boolean removeTeam(CommandSender staff, String reason, Player player)
    {
        return removeTeam(staff, reason, player, UserManager.getUser(player.getUniqueId()));
    }

    /**
     * Removes the player from their current team and adds them to the new one, no matter what.
     */
    public static void forceSetTeam(CommandSender staff, String reason, Player player, User user, Team team)
    {
        if (user.getTeam() != null)
        {
            removeTeamCleared(staff, reason, player, user);
        }

        // If the team passed is null, we don't have to do anything extra, since we'd
        // just be removing their team of the player, and that's already done.

        if (team != null)
        {
            addTeamCleared(staff, reason, player, user, team);
        }
    }
}
