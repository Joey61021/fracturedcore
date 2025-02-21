package com.fractured.user;

import com.fractured.team.Team;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
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
    /**
     * Last region alert location to prevent spam
     */
    private Location lastAlert;
    private boolean bypassRegions;
    private long lastMessageTimestamp = 0;

    public User(UUID uuid)
    {
        this.uuid = uuid;
        lastAlert = null;
        bypassRegions = false;
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

    public void setLastAlert(Location location)
    {
        this.lastAlert = location;
    }

    public Location getLastAlert()
    {
        return lastAlert;
    }

    public void setBypassRegions(boolean bypassRegions)
    {
        this.bypassRegions = bypassRegions;
    }

    public boolean getBypassRegions()
    {
        return bypassRegions;
    }

    public long getLastMessageTimestamp()
    {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp()
    {
        this.lastMessageTimestamp = System.currentTimeMillis();
    }
}
