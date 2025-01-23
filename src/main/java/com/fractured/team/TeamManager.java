package com.fractured.team;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.util.globals.Messages;
import com.fractured.util.Utils;
import org.bukkit.entity.Player;

public class TeamManager
{
    public static void toggleTeamChat(Player player, User user)
    {
        Team team = user.getTeam();

        if (team == null)
        {
            player.sendMessage(Messages.CMD_TC_NOT_IN_TEAM);
        } else if (user.isInTeamChat())
        {
            user.setInTeamChat(false);
            player.sendMessage(Messages.CMD_TC_TOGGLE_OFF);
        } else
        {
            user.setInTeamChat(true);
            player.sendMessage(Messages.CMD_TC_TOGGLE_ON);
        }
    }

    public static void setTeam(Player player, Team team)
    {
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());

        if (team == null)
        {
            // Removing them from a team
            player.sendMessage("You have been removed from " + user.getTeam().getName() + " team.");

        } else
        {
            // Adding them to a team
            player.teleport(team.spawn());

            player.setPlayerListName(team.color() + player.getName());
            player.setDisplayName(team.color() + player.getDisplayName());
            player.setPlayerListFooter(Utils.color("&7Your team: " + team.color() + team.getName() + " team"));

            FracturedCore.runAsync(() ->
            {
                FracturedCore.getStorage().assignTeam(user, team);
            });

            player.sendMessage("Joined " + team.getName() + " team...");
        }
        user.setTeam(team);
    }
}
