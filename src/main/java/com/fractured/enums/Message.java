package com.fractured.enums;

import lombok.Getter;

public enum Message {
    NO_PERMISSION("&cYou do not have permission to run this command!"),
    CONSOLE_BLOCKED("&cThis command cannot be run through console!"),
    TEAM_INVENTORY_CLOSED("&cTo get started, join a team with /team"),
    INVALID_ARG("&cInvalid argument!"),
    FRIENDLY_FIRE_DISABLED("&cFriendly fire is disabled!"),
    UNABLE_TO_BUILD("&cYou are unable to build without enabling build mode with /build first!"),

    CMD_TC_TOGGLE_ON("&fTeam chat has been toggled &aON"),
    CMD_TC_TOGGLE_OFF("&fTeam chat has been toggled &cOFF"),
    CMD_TC_NOT_IN_TEAM("&cPlease join a team before connecting to a team chat!"),

    CMD_TEAM_ALREADY_IN_TEAM("&cYou are already in a team!"),

    REGION_TEAM_ALERTED("&4The %team% team has been alerted of your location."),
    REGION_TEAM_OFFLINE("&cYou are unable to complete this action while this team is offline!"),
    REGION_ALERT_BLOCK_PLACE("&4&l!!! &c%player% has placed a block in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% &4&l!!!"),
    REGION_ALERT_BLOCK_BREAK("&4&l!!! &c%player% has broken a block in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% &4&l!!!"),
    REGION_ALERT_CHEST_OPEN("&4&l!!! &c%player% has chest has been opened in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% &4&l!!!"),

    CMD_BUILD_TOGGLE_ON("&fBuild is now toggled &aON&f!"),
    CMD_BUILD_TOGGLE_OFF("&fBuild is now toggled &cOFF&f!"),

    CMD_BYPASS_REGIONS_TOGGLE_ON("&cYou are now bypassing region restrictions."),
    CMD_BYPASS_REGIONS_TOGGLE_OFF("&eYou are no longer bypassing region restrictions."),

    CMD_SETTING_TOGGLE("&b%val% &fhas been toggled %bool%&f!"),

    CMD_SET_LOCATION_INVALID("&cInvalid location, please double check your arguments!"),
    CMD_SET_LOCATION_SET("&fSuccessfully set location for &b%location%&f!"),

    CMD_GENERATE_BORDER_CONFIRM("&cAre you sure you want to generate world borders at your current location? Doing so will cause lag to the server. Please execute /generateborders confirm to proceed."),
    CMD_GENERATE_BORDER_GENERATING("&7Generating, please wait..."),
    CMD_GENERATE_BORDER_COMPLETE("&aBorder generated successfully!"),

    CMD_REGION_CHECK("&fYou are currently in %team% team's &fregion!"),

    REGION_SELECTED("&f%pos% has been selected at your location for %team%&f!"),
    REGION_CYCLE_TEAM("&fTeam cycled to %team%&f!");

    @Getter private final String message;

    Message(String message) {
        this.message = message;
    }
}
