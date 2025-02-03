package com.fractured.util;

import org.bukkit.entity.Player;

public class TPRequest {

    private Player sender;
    private Player receiver;

    public TPRequest(Player sender, Player receiver)
    {
        this.sender = sender;
        this.receiver = receiver;
    }
}
