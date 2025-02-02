package com.fractured.commands;

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
        teamSubCommands.register("clear", TeamCommand::teamClearCommand, "c", "remove", "r");
        teamSubCommands.register("set", TeamCommand::teamSetCommand, "add", "a", "s");
        teamSubCommands.register("forceset", TeamCommand::teamForceSetCommand, "fs");
        teamSubCommands.register("join", TeamCommand::teamJoinCommand, "j");
        teamSubCommands.register("forcejoin", TeamCommand::teamForceJoinCommand, "fj");
        teamSubCommands.register("menu", TeamCommand::teamMenuCommand, "m");
        teamSubCommands.register("list", TeamCommand::teamList, "l");
        teamSubCommands.register("teleport", TeamCommand::teamTeleport, "tp");
    }

    public static boolean team(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (args.length == 0 || !teamSubCommands.dispatch(sender, args))
        {
            if (sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
            {
                sender.sendMessage(Messages.COMMAND_TEAM_GENERAL_STAFF_USAGE);
            } else
            {
                sender.sendMessage(Messages.COMMAND_TEAM_GENERAL_USAGE);
            }
        }
        return true;
    }

    private static void teamTeleport(final CommandSender sender, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CONSOLE_BLOCKED));
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
            sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
            return;
        }

        ((Player) sender).teleport(team.spawn());
    }

    // SUB COMMANDS

    private static void teamList(final CommandSender sender, String[] args)
    {
        Collection<Team> teams = TeamCache.teams();

        // fixme store message and update as teams are added
        StringBuilder teamList = new StringBuilder();

        for (Team team : teams)
        {
            teamList.append(ChatColor.GRAY);
            teamList.append('(');
            teamList.append(team.getOnlineMembers().size());
            teamList.append('/');
            teamList.append(team.getTotalMembers());
            teamList.append(")  ").append(team.color()).append(team.getName());
        }

        sender.sendMessage(teamList.toString());
    }

    private static void teamClearCommand(final CommandSender sender, String[] args)
    {
        // /team clear [username]

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return;
        }

        // clear, and user make 2
        if (args.length < 2)
        {
            sender.sendMessage(Messages.COMMAND_TEAM_CLEAR_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TARGET);
            return;
        }

        User user = UserManager.getUser(target.getUniqueId());

        if (!TeamManager.removeTeam(sender, null, target, user))
        {
            // fixme
            sender.sendMessage("Target not in team!");
        }
    }

    private static void teamSetCommand(final CommandSender sender, String[] args)
    {
        // /team set [user] [team | id]
        // console allowed,

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return;
        }

        // set, user, and team makes 3
        if (args.length < 3)
        {
            sender.sendMessage(Messages.COMMAND_TEAM_SET_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TARGET);
            return;
        }

        User user = UserManager.getUser(target.getUniqueId());

        switch (TeamManager.addTeam(sender, null, target, user, args[2]))
        {
            case 0:
                // todo
                sender.sendMessage("Added.");
                return;
            case 1:
                sender.sendMessage(Messages.COMMAND_TARGET_ALREADY_IN_TEAM);
                return;
            case 2:
                sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
                return;
        }
    }

    private static void teamForceSetCommand(final CommandSender sender, String[] args)
    {
        // /team set [user] [team | id]
        // console allowed,

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return;
        }

        // set, user, and team makes 3
        if (args.length < 3)
        {
            sender.sendMessage(Messages.COMMAND_TEAM_SET_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TARGET);
            return;
        }

        User user = UserManager.getUser(target.getUniqueId());

        Team team = TeamCache.getTeam(args[2]);

        if (team == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
            return;
        }

        TeamManager.forceSetTeam(sender, null, target, user, team);
    }

    private static void teamJoinCommand(final CommandSender sender, String[] args)
    {
        // /team join [team | id]
        // anyone can execute this except for console

        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CONSOLE_BLOCKED));
            return;
        }

        Player player = (Player) sender;
        User user = UserManager.getUser(player.getUniqueId());

        switch (TeamManager.addTeam(sender, null, player, user, args[1]))
        {
            case 0:
                // todo
                sender.sendMessage("Joined!");
            case 1:
                sender.sendMessage(Messages.COMMAND_TEAM_ALREADY_IN_TEAM);
            case 2:
                sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
        }
    }

    private static void teamForceJoinCommand(final CommandSender sender, String[] args)
    {
        // /team forcejoin [team | id]
        // anyone can execute this except for console

        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CONSOLE_BLOCKED));
            return;
        }

        Player player = (Player) sender;
        User user = UserManager.getUser(player.getUniqueId());

        Team team = TeamCache.getTeam(args[1]);
        if (team == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
            return;
        }

        TeamManager.forceSetTeam(sender, null, player, user, team);
    }

    private static void teamMenuCommand(final CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CONSOLE_BLOCKED));
            return;
        }

        Player player = (Player) sender;
        User user = UserManager.getUser(player.getUniqueId());

        if (user.getTeam() != null)
        {
            sender.sendMessage(Messages.COMMAND_TEAM_ALREADY_IN_TEAM);
            return;
        }

        TeamCache.openMenu(player);
    }
}
