package com.fractured.enums;

import com.fractured.managers.LocationManager;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public enum AlertReason {
    BLOCK_PLACE(Message.REGION_ALERT_BLOCK_PLACE.getMessage()),
    BLOCK_BREAK(Message.REGION_ALERT_BLOCK_BREAK.getMessage());

    @Getter private final String message;

    AlertReason(String message) {
        this.message = message;
    }
}
