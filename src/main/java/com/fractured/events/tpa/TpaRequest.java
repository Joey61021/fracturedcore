package com.fractured.events.tpa;

import java.util.UUID;

public class TpaRequest {

    private final boolean to;
    private final UUID requester;
    private final UUID requested;
    private final long timestamp;

    public TpaRequest(boolean to, UUID requester, UUID requested)
    {
        this.to = to; // requester is teleporting to requested?
        this.requester = requester;
        this.requested = requested;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean getTo()
    {
        return to;
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
