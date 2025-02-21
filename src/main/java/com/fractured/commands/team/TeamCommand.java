package com.fractured.commands.team;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.team.Team;
import com.fractured.team.TeamCache;
import com.fractured.team.TeamManager;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class TeamCommand
{
    private TeamCommand()
    {
    }

    private static final SubCommandRegistry teamSubCommands;

    static
    {
        teamSubCommands = new SubCommandRegistry(0);

        /*
        /team menu

        Lets you join a team (assuming you're not already in one)
        /team join [name | id]

        Staff command that allows you to set another player's team (mainly for bug fixes
        or any player goofs) This command fails if the user is already in a team
        /team set [username] [name | id]

        Staff command that forcefully sets another player's team, kicking them from their
        current team if necessary.
        /team forceset [user] [name | id]

        Staff command that lets you remove a player from a team
        /team clear [username]

        Staff command that puts you in a team, removing you from the team you're already in
        /team forcejoin [name | id]
         */
        teamSubCommands.register("clear", TeamCommand::teamClear, "c", "remove", "r");
        teamSubCommands.register("set", TeamCommand::teamSet, "add", "a", "s");
        teamSubCommands.register("forceset", TeamCommand::teamForceSet, "fs");
        teamSubCommands.register("join", TeamCommand::teamJoin, "j");
        teamSubCommands.register("forcejoin", TeamCommand::teamForceJoin, "fj");
        teamSubCommands.register("menu", TeamCommand::teamMenu, "m");
        teamSubCommands.register("list", TeamCommand::teamList, "l");
        teamSubCommands.register("teleport", TeamCommand::teamTeleport, "tp");
        teamSubCommands.register("setspawn", TeamCommand::setSpawn);
    }

    public static boolean team(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (args.length == 0)
        {
            teamMenu(sender, cmd, label, args);
            return true;
        }

        if (teamSubCommands.dispatch(sender, cmd, label, args))
        {
            if (sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_GENERAL_STAFF_USAGE));
            } else
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_GENERAL_USAGE));
            }
        }
        return true;
    }

    private static void teamSetSpawn(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {

    }

    private static void teamTeleport(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return;
        }

        // /team tp [team] args.length == 2

        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_TELEPORT_USAGE));
            return;
        }

        Team team = TeamCache.getTeamByPhrase(args[1]);

        if (team == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TEAM));
            return;
        }

        ((Player) sender).teleport(team.spawn());
    }

    private static void teamList(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        Collection<Team> teams = TeamCache.teams();

        // fixme store message and update as teams are added
        StringBuilder teamList = new StringBuilder();

        for (Team team : teams)
        {
            teamList.append(ChatColor.GRAY)
                    .append('(')
                    .append(team.getOnlineMembers().size())
                    .append('/')
                    .append(team.getTotalMembers())
                    .append(")  ")
                    .append(team.color())
                    .append(team.getName())
                    .append('\n');
        }

        sender.sendMessage(teamList.toString());
    }

    private static void teamClear(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /team clear [username]

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        // clear, and user make 2
        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_REMOVE_USAGE));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TARGET));
            return;
        }

        User user = UserManager.getUser(target.getUniqueId());
        Team team = user.getTeam(); // To be displayed later

        if (!TeamManager.removeTeam(sender, null, target, user))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_REMOVE_NOT_IN_TEAM));
        } else
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_REMOVE_TARGET_REMOVED));
            target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_REMOVE_REMOVED).replace("%team%", team.color() + team.getName()));
        }
    }

    private static void teamSet(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /team set [user] [team | id]
        // console allowed,

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        // set, user, and team makes 3
        if (args.length < 3)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_SET_USAGE));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TARGET));
            return;
        }

        User user = UserManager.getUser(target.getUniqueId());

        switch (TeamManager.addTeam(sender, null, target, user, args[2]))
        {
            // TeamManager#addTeam sends a message for case 0
            case 1:
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TARGET_ALREADY_IN_TEAM));
                break;
            case 2:
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TEAM));
                break;
        }
    }

    private static void teamForceSet(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /team set [user] [team | id]
        // console allowed,

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        // set, user, and team makes 3
        if (args.length < 3)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_FORCE_SET_USAGE));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TARGET));
            return;
        }

        User user = UserManager.getUser(target.getUniqueId());
        Team team = TeamCache.getTeam(args[2]);

        if (team == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_FORCE_SET_INVALID_TEAM));
            return;
        }

        TeamManager.forceSetTeam(sender, null, target, user, team);
    }

    private static void teamJoin(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /team join [team | id]
        // anyone can execute this except for console

        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return;
        }

        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_JOIN_USAGE));
            return;
        }

        Player player = (Player) sender;
        User user = UserManager.getUser(player.getUniqueId());

        switch (TeamManager.addTeam(sender, null, player, user, args[1]))
        {
            // Message is sent by TeamManager#addTeam for case 0
            case 1:
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_ALREADY_IN_TEAM));
                break;
            case 2:
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TEAM));
                break;
        }
    }

    private static void teamForceJoin(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /team forcejoin [team | id]
        // anyone can execute this except for console

        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return;
        }

        Player player = (Player) sender;
        User user = UserManager.getUser(player.getUniqueId());

        Team team = TeamCache.getTeam(args[1]);
        if (team == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TEAM));
            return;
        }

        TeamManager.forceSetTeam(sender, null, player, user, team);
    }

    private static void teamMenu(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return;
        }

        Player player = (Player) sender;
        User user = UserManager.getUser(player.getUniqueId());

        if (user.getTeam() != null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_ALREADY_IN_TEAM));
            return;
        }

        TeamCache.openMenu(player);
    }

    private static void setSpawn(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // /team set [user] [team | id]
        // console not allowed,

        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return;
        }

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        // setspawn, team, makes 2
        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_FORCE_SET_USAGE));
            return;
        }

        Team team = TeamCache.getTeam(args[1]);

        if (team == null) {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_INVALID_TEAM));
            return;
        }

        // todo: confirmation
        Location loc = ((Player) sender).getLocation();

        team.setSpawn(loc);

        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TEAM_SET_SPAWN_SET)
                .replace("%team%", team.getName())
                .replace("%x%", String.valueOf(loc.getX()))
                .replace("%y%", String.valueOf(loc.getY()))
                .replace("%z%", String.valueOf(loc.getZ())));
    }
}
