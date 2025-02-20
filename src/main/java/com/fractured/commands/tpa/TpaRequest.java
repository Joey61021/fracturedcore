package com.fractured.commands.tpa;

import java.util.UUID;

public class TpaRequest {

    private UUID requester;
    private UUID requested;
    private long timestamp;

    public TpaRequest(UUID requester, UUID requested)
    {
        this.requester = requester;
        this.requested = requested;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getRequester() {
        return requester;
    }

    public UUID getRequested() {
        return requested;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
