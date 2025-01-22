package com.fractured.managers.message;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Message {

    NO_PERMISSION("generic.no-permission"),
    CONSOLE_BLOCKED("generic.console-blocked"),
    TEAM_INVENTORY_CLOSED("generic.team-inventory-closed"),
    INVALID_ARG("generic.invalid-arg"),
    FRIENDLY_FIRE_DISABLED("generic.friendly-fire-disabled"),
    UNABLE_TO_BUILD("generic.unable-to-build"),
    ALREADY_IN_TEAM("generic.already-in-team"),

    REGION_TEAM_ALERTED("generic.region.team-alerted"),
    REGION_TEAM_OFFLINE("generic.region.team-offline"),
    REGION_ALERT_BLOCK_PLACE("generic.region.block-place"),
    REGION_ALERT_BLOCK_BREAK("generic.region.block-break"),
    REGION_ALERT_CHEST_OPEN("generic.region.chest-open"),

    REGION_WAND_SELECTED("generic.region-wand.selected"),
    REGION_WAND_CYCLED("generic.region-wand.cycled"),

    CMD_TC_TOGGLE_ON("commands.team-chat.toggle-on"),
    CMD_TC_TOGGLE_OFF("commands.team-chat.toggle-off"),
    CMD_TC_NOT_IN_TEAM("commands.not-in-team"),

    CMD_BUILD_TOGGLE_ON("commands.build.toggle-on"),
    CMD_BUILD_TOGGLE_OFF("commands.build.toggle-off"),

    CMD_BYPASS_REGIONS_TOGGLE_ON("commands.bypass-regions.toggle-on"),
    CMD_BYPASS_REGIONS_TOGGLE_OFF("commands.bypass-regions.toggle-off"),

    CMD_SETTING_TOGGLE("commands.settings-toggled"),

    CMD_SET_LOCATION_INVALID("commands.set-location.invalid"),
    CMD_SET_LOCATION_SET("commands.set-location.set"),

    CMD_SPAWN_NO_TEAM("commands.spawn.no-team"),
    CMD_SPAWN_NOT_IN_REGION("commands.spawn.not-in-region"),
    CMD_SPAWN_TELEPORTED("commands.spawn.teleported"),

    CMD_GENERATE_BORDERS_CONFIRM("commands.generate-borders.confirm"),
    CMD_GENERATE_BORDERS_GENERATING("commands.generate-borders.generating"),
    CMD_GENERATE_BORDERS_COMPLETE("commands.generate-borders.complete"),

    CMD_REGION_CHECK("commands.region-check");

    @Getter
    @NonNull
    private final String path;
}
