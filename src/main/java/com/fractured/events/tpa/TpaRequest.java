package com.fractured.events.tpa;

import java.util.UUID;

public class TpaRequest {

    private final boolean tpaHere;
    private final UUID requester;
    private final UUID requested;
    private final long timestamp;

    public TpaRequest(boolean tpaHere, UUID requester, UUID requested)
    {
        this.tpaHere = tpaHere; // requester is teleporting to requested?
        this.requester = requester;
        this.requested = requested;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean getTpaHere()
    {
        return tpaHere;
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
