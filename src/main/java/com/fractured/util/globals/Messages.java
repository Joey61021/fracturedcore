package com.fractured.util.globals;

import com.fractured.config.keys.TextConfigKey;
import com.fractured.util.Utils;
import net.md_5.bungee.api.ChatColor;

import static com.fractured.config.DataSupplier.textKey;

public final class Messages
{
    private Messages()
    {

    }

    public static final TextConfigKey TEAM_INVENTORY_CLOSED = textKey("generic.team_inventory_closed");
    public static final TextConfigKey INVALID_ARG = textKey("generic.invalid_arg");
    public static final TextConfigKey FRIENDLY_FIRE_DISABLED = textKey("generic.friendly_fire_disabled");
    public static final TextConfigKey ALREADY_IN_TEAM = textKey("generic.already_in_team");

    public static final TextConfigKey COMMAND_TEAM_TELEPORT_USAGE = textKey("commands.team.teleport.usage");

    public static final TextConfigKey NO_PERMISSION = textKey("commands.no_permission");
    public static final TextConfigKey CONSOLE_BLOCKED = textKey("commands.console_blocked");
    public static final TextConfigKey NO_TEAM_BLOCKED = textKey("commands.spawn.no_team");

    public static final TextConfigKey REGION_TEAM_ALERTED = textKey("generic.region.team_alerted");
    public static final TextConfigKey REGION_TEAM_OFFLINE = textKey("generic.region.team_offline");
    public static final TextConfigKey REGION_ALERT_BLOCK_PLACE = textKey("generic.region.block_place");
    public static final TextConfigKey REGION_ALERT_BLOCK_BREAK = textKey("generic.region.block_break");
    public static final TextConfigKey REGION_ALERT_CHEST_OPEN = textKey("generic.region.chest_open");

    public static final TextConfigKey UPGRADES_MAX_LEVEL = textKey("upgrades.max_level");
    public static final TextConfigKey UPGRADES_NOT_ENOUGH = textKey("upgrades.not_enough");
    public static final TextConfigKey UPGRADES_UPGRADED = textKey("upgrades.upgraded");

    public static final TextConfigKey CMD_TC_TOGGLE_ON = textKey("commands.team_chat.toggle_on");
    public static final TextConfigKey CMD_TC_TOGGLE_OFF = textKey("commands.team_chat.toggle_off");
    public static final TextConfigKey CMD_TC_NOT_IN_TEAM = textKey("commands.not_team_blocked");

    public static final TextConfigKey CMD_SET_LOCATION_INVALID = textKey("commands.set-location.invalid");
    public static final TextConfigKey CMD_SET_LOCATION_SET = textKey("commands.set-location.set");

    public static final TextConfigKey CMD_SPAWN_NOT_IN_REGION = textKey("commands.spawn.not-in-region");
    public static final TextConfigKey CMD_SPAWN_TELEPORTED = textKey("commands.spawn.teleported");

    public static final TextConfigKey COMMAND_BORDERS_USAGE = textKey("commands.borders.usage");
    public static final TextConfigKey COMMAND_BORDERS_GENERATE_CONFIRM = textKey("commands.borders.generate.confirm");
    public static final TextConfigKey COMMAND_BORDERS_GENERATE_START = textKey("commands.borders.generate.generating");
    public static final TextConfigKey COMMAND_BORDERS_GENERATE_END = textKey("commands.borders.generate.complete");
    public static final TextConfigKey COMMAND_BORDERS_EXTEND_CONFIRM = textKey("commands.borders.extend.confirm");
    public static final TextConfigKey COMMAND_BORDERS_EXTEND_START = textKey("commands.borders.generate.generating");
    public static final TextConfigKey COMMAND_BORDERS_EXTEND_END = textKey("commands.borders.generate.complete");

    public static final TextConfigKey COMMAND_SETTINGS_CONSOLE_USAGE = textKey("commands.settings.console_usage");
    public static final TextConfigKey COMMAND_SETTINGS_PLAYER_USAGE = textKey("commands.settings.player_usage");
    public static final TextConfigKey COMMAND_SETTINGS_SET = textKey("commands.settings.set");
    public static final TextConfigKey COMMAND_SETTINGS_SET_INVALID_PATH = textKey("commands.settings.set.invalid_path");

    public static final TextConfigKey COMMAND_DISCORD = textKey("commands.discord");

    public static final String TAB_LIST_HEADER = Utils.color("&eFractured &f| &e1.21.4 SMP");
    public static final String NO_TEAM_TAB_LIST_FOOTER = Utils.color("&7Your team: None");

    public static final String COMMAND_INVALID_TARGET = ChatColor.RED + "Invalid target.";

    public static final String COMMAND_TEAM_CLEAR_USAGE = ChatColor.RED + "Usage: /team remove [username]";
    public static final String COMMAND_TEAM_SET_USAGE = ChatColor.RED + "Usage: /team set [username] [team | team_id]";
    public static final String COMMAND_TEAM_GENERAL_USAGE = ChatColor.RED + "Usage:\n- /team menu\n- /team join [team | team_id]";
    public static final String COMMAND_TEAM_GENERAL_STAFF_USAGE = ChatColor.RED + "Usage:\n- /team menu\n- /team join [team | team_id]\n- /team set [username] [team | team_id]\n- /team forceset [username] [team | team_id]\n- /team forcejoin [team | team_id]\n- /team list\n- /team remove [username]\n- /team teleport [team | team_id]";
    public static final String COMMAND_TARGET_ALREADY_IN_TEAM = ChatColor.RED + "Failed to set player team: Target is already in team";
    public static final String COMMAND_TEAM_ALREADY_IN_TEAM = ChatColor.RED + "You are already in a team! If you think you made a mistake or this should be changed, please contact the server owners.";
    public static final String COMMAND_INVALID_TEAM = ChatColor.RED + "Unable to find the specified team.";

    public static final String PLUGIN_RESTARTING = ChatColor.YELLOW + "Server Restarting...";
}
