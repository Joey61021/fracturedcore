package com.fractured.trades;

import java.util.UUID;

public class TradeRequest {

    private final UUID requester;
    private final UUID requested;
    private final long timestamp;

    public TradeRequest(UUID requester, UUID requested)
    {
        this.requester = requester;
        this.requested = requested;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getRequester()
    {
        return requester;
    }

    public UUID getRequested()
    {
        return requested;
    }

    public long getTimestamp()
    {
        return timestamp;
    }
}
