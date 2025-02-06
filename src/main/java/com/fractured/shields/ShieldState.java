package com.fractured.shields;

public enum ShieldState {

    ACTIVE("&aActive"),
    INACTIVE("&cInactive"),
    AWAITING_CHANGE("&bAwaiting &7(shift-click to lock)");

    ShieldState(String name)
    {
        this.name = name;
    }

    private final String name;

    public String getName()
    {
        return name;
    }
}
