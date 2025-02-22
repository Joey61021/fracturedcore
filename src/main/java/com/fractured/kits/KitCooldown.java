package com.fractured.kits;

import java.util.UUID;

public class KitCooldown {

    private final UUID uuid;
    private final int kitId;
    private final long timestamp;

    public KitCooldown(UUID uuid, int kitId, int cooldown)
    {
        this.uuid = uuid;
        this.kitId = kitId;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public int getKitId()
    {
        return kitId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }
}
