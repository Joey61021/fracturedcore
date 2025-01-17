package com.fractured.enums;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Message {
    NO_PERMISSION("&cYou do not have permission to run this command!"),
    CONSOLE_BLOCKED("&cThis command cannot be run through console!"),
    TEAM_INVENTORY_CLOSED("&cTo get started, join a team with /team"),

    CMD_GM_SURVIVAL("&fYour gamemode has been changed to &aSurvival&f!"),
    CMD_GM_CREATIVE("&fYour gamemode has been changed to &cCreative&f!"),

    CMD_TC_TOGGLE_ON("&fTeam chat has been toggled &aON"),
    CMD_TC_TOGGLE_OFF("&fTeam chat has been toggled &cOFF"),
    CMD_TC_NOT_IN_TEAM("&cPlease join a team before connecting to a team chat!"),

    CMD_TEAM_ALREADY_IN_TEAM("&cYou are already in a team!"),

    REGION_TEAM_ALERTED("&4The %team% team has been alerted of your location."),
    REGION_TEAM_OFFLINE("&cYou are unable to complete this action while this team is offline!"),
    REGION_ALERT_BLOCK_PLACE("&4&l!!! &c%player% has placed a block in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% &4&l!!!"),
    REGION_ALERT_BLOCK_BREAK("&4&l!!! &c%player% has broken a block in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% &4&l!!!"),
    REGION_ALERT_CHEST_OPEN("&4&l!!! &c%player% has chest has been opened in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% &4&l!!!");

    @Getter private final String message;

    Message(String message) {
        this.message = message;
    }
}
