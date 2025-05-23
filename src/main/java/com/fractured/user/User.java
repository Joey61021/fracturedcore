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
    private long lastAlert = 0;
    private boolean bypassRegions = false;
    private long lastMessageTimestamp = 0;
    private boolean socialSpy = false;
    private UUID lastMessage = null;
    private String lastChatMessage = "";
    private String tag = null;
    private UUID selectedPlayer = null; // used in admin menus
    private Location lastLocation = null; // /back command

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

    public void setLastAlert()
    {
        this.lastAlert = System.currentTimeMillis();
    }

    public long getLastAlert()
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

    public boolean getSocialSpy()
    {
        return this.socialSpy;
    }

    public void setSocialSpy(boolean socialSpy)
    {
        this.socialSpy = socialSpy;
    }

    public UUID getLastMessage()
    {
        return this.lastMessage;
    }

    public void setLastMessage(UUID lastMessage)
    {
        this.lastMessage = lastMessage;
    }

    public String getLastChatMessage()
    {
        return lastChatMessage;
    }

    public void setLastChatMessage(String lastChatMessage)
    {
        this.lastChatMessage = lastChatMessage;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public UUID getSelectedPlayer()
    {
        return selectedPlayer;
    }

    public void setSelectedPlayer(UUID selectedPlayer)
    {
        this.selectedPlayer = selectedPlayer;
    }

    public Location getLastLocation()
    {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation)
    {
        this.lastLocation = lastLocation;
    }
}
