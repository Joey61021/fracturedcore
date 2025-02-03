package com.fractured.cevents;

import net.md_5.bungee.api.ChatColor;

public class CEvent {

    public int id = 0;

    public ChatColor color;
    public String name;
    public int time;
    public int minimum_players = 1;

    public CEvent(ChatColor color, String name, int time)
    {
        this.id = EventManager.events.size()+1;

        this.color = color;
        this.name = name;
        this.time = time; // Time in minutes
    }

    public ChatColor getColor()
    {
        return this.color;
    }

    public String getName()
    {
        return this.name;
    }

    public int getTime()
    {
        return this.time;
    }

    public int getMinimumPlayers() {
        return this.minimum_players;
    }
}
