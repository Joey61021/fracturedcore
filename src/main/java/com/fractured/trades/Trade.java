package com.fractured.trades;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class Trade {

    private final int id;
    private final UUID player;
    private final Inventory inventory;

    public Trade(int id, UUID player, Inventory inventory)
    {
        this.id = id;
        this.player = player;
        this.inventory = inventory;
    }

    public int getId()
    {
        return this.id;
    }

    public UUID getUUID()
    {
        return this.player;
    }
}
