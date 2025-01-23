package com.fractured.user;

import com.fractured.team.Team;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User
{
    private final UUID uuid;
    /**
     * As in the database
     */
    private int userId;
    private boolean inTeamChat;
    private Team team;

    public User(UUID uuid)
    {
        this.uuid = uuid;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getUserId()
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
     * Use {@link com.fractured.team.TeamManager#setTeam(Player, Team)}
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
