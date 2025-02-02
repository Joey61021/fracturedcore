package com.fractured.user;

import com.fractured.team.Team;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class User
{
    private final UUID uuid;
    /**
     * As in the database
     */
    private int userId;
    private boolean inTeamChat;
    private Team team;
    /**
     * Mapping the upgrade_id to the upgrade level
     */
    private final Map<Integer, Integer> upgrades;

    public User(UUID uuid)
    {
        this.uuid = uuid;
        upgrades = new HashMap<>();
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getId()
    {
        return userId;
    }

    public boolean isInTeamChat()
    {
        return inTeamChat;
    }

    public void setInTeamChat(boolean inTeamChat)
    {
        this.inTeamChat = inTeamChat;
    }

    public Team getTeam()
    {
        return team;
    }

    /**
     * Use {@link com.fractured.team.TeamManager#addTeam(Player, Team)} or {@link com.fractured.team.TeamManager#removeTeam(CommandSender, String, Player)} )}
     */
    public void setTeam(Team team)
    {
        this.team = team;
        // This makes it so that you have to retoggle team chat if you join a new team.
        // This also guarentees that you can't be in teamchat if you don't have a team,
        // (coupled with a check that checks before you set in team chat if you are in
        // a team)
        inTeamChat = false;
    }
}
