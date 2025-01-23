package com.fractured.util.globals;

import com.fractured.util.Utils;
import net.md_5.bungee.api.ChatColor;

public final class Messages
{
    private Messages()
    {

    }

    public static final String TAB_LIST_HEADER = Utils.color("&eFractured &f| &e1.21.4 SMP");
    public static final String NO_TEAM_TAB_LIST_FOOTER = Utils.color("&7Your team: None");

    public static final String COMMAND_INVALID_TARGET = ChatColor.RED + "Invalid target.";

    public static final String COMMAND_TEAM_CLEAR_USAGE = ChatColor.RED + "Usage: /team remove [username]";
    public static final String COMMAND_TEAM_SET_USAGE = ChatColor.RED + "Usage: /team set [username] [team | team_id]";
    public static final String COMMAND_TEAM_GENERAL_USAGE = ChatColor.RED + "Usage:\n- /team menu\n- /team join [team | team_id]";
    public static final String COMMAND_TEAM_GENERAL_STAFF_USAGE = ChatColor.RED + "Usage:\n- /team menu\n- /team join [team | team_id]\n- /team set [username] [team | team_id]\n- /team forceset [username] [team | team_id]\n- /team forcejoin [team | team_id]\n- /team list\n- /team remove [username]";
    public static final String COMMAND_TEAM_SET_TARGET_ALREADY_IN_TEAM = ChatColor.RED + "Failed to set player team: Target is already in team";
    public static final String COMMAND_TEAM_ALREADY_IN_TEAM = ChatColor.RED + "You are already in a team! If you think you made a mistake or this should be changed, please contact the server owners.";
    public static final String COMMAND_INVALID_TEAM = ChatColor.RED + "Unable to find the specified team.";

    public static final String PLUGIN_RESTARTING = ChatColor.YELLOW + "Server Restarting...";
    public static final String NO_PERMISSION = ChatColor.RED + "You do not have permission to run this command!";
    public static final String CONSOLE_BLOCKED = ChatColor.RED + "This command cannot be run through console!";
    public static final String TEAM_INVENTORY_CLOSED = ChatColor.RED + "To get started, join a team with /team";

    public static final String CMD_GM_SURVIVAL = ChatColor.WHITE + "Your gamemode has been changed to " + ChatColor.GREEN + "Survival" + ChatColor.WHITE + "!";
    public static final String CMD_GM_CREATIVE = ChatColor.WHITE + "Your gamemode has been changed to " + ChatColor.RED + "Creative" + ChatColor.WHITE + "!";

    public static final String CMD_TC_TOGGLE_ON = ChatColor.WHITE + "Team chat has been toggled " + ChatColor.GREEN + "ON";
    public static final String CMD_TC_TOGGLE_OFF = ChatColor.WHITE + "Team chat has been toggled " + ChatColor.RED + "OFF";
    public static final String CMD_TC_NOT_IN_TEAM = ChatColor.RED + "Please join a team before connecting to a team chat!";

    public static final String REGION_TEAM_ALERTED = ChatColor.DARK_RED + "The %team% team has been alerted of your location.";
    public static final String REGION_TEAM_OFFLINE = ChatColor.RED + "You are unable to complete this action while this team is offline!";
    public static final String REGION_ALERT_BLOCK_PLACE = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "!!! " +
            ChatColor.RED + "%player% has placed a block in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% " +
            ChatColor.DARK_RED + ChatColor.BOLD + "!!!";
    public static final String REGION_ALERT_BLOCK_BREAK = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "!!! " +
            ChatColor.RED + "%player% has broken a block in %team% team's region at X: %locx%, Y: %locy%, Z: %locz% " +
            ChatColor.DARK_RED + ChatColor.BOLD + "!!!";

}
