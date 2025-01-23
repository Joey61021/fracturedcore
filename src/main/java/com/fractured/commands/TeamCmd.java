package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.team.Team;
import com.fractured.team.TeamCache;
import com.fractured.team.TeamManager;
import com.fractured.user.User;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TeamCmd implements CommandExecutor
{
    private static SubCommandRegistry subCommands;

    public TeamCmd()
    {
        if (subCommands != null)
        {
            // The fact that I have to write this is just sad
            throw new IllegalStateException("Cannot instantiate TeamCmd more than once!");
        }

        subCommands = new SubCommandRegistry(0);

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
        subCommands.register("clear", TeamCmd::teamClearCommand, "c", "remove", "r");
        subCommands.register("set", TeamCmd::teamSetCommand, "s");
        subCommands.register("forceset", TeamCmd::teamForceSetCommand, "fs");
        subCommands.register("join", TeamCmd::teamJoinCommand, "j");
        subCommands.register("forcejoin", TeamCmd::teamForceJoinCommand, "fj");
        subCommands.register("menu", TeamCmd::teamMenuCommand, "m");
        subCommands.register("list", TeamCmd::teamListCommand, "l");
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (args.length == 0 || !subCommands.dispatch(sender, args))
        {
            if (sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
            {
                sender.sendMessage(Messages.COMMAND_TEAM_GENERAL_STAFF_USAGE);
            } else
            {
                sender.sendMessage(Messages.COMMAND_TEAM_GENERAL_USAGE);
            }
            return true;
        }
        return true;
    }

    // SUB COMMANDS

    private static void teamListCommand(final CommandSender sender, String[] args)
    {
        Collection<Team> teams = TeamCache.teams();

        // fixme store message and update as teams are added
        StringBuilder teamList = new StringBuilder();
        String s;

        for (Team team : teams)
        {
            // I've never done this before, but it's pretty cool. This is how you can do alignment
            s = Integer.toString(team.getTeamId());
            teamList.append(ChatColor.WHITE).append(s);

            for (int i = 0; i < s.length() - 3; ++i)
            {
                teamList.append(" ");
            }

            s = team.getName();

            teamList.append(team.color()).append(s);
            // team.color() is not included in this here
            for (int i = 0; i < s.length() - 8; ++i)
            {
                teamList.append(" ");
            }

            teamList.append(ChatColor.GRAY);
            teamList.append('(');
            teamList.append(team.getOnlineMembers().size());
            teamList.append('/');
            teamList.append(team.getTotalMembers());
            teamList.append(")\n");
        }

        // 3     8
        //|-||------|
        //1  Blue    (1/4)
        //2  Yellow  (0/5)
        //3  Red     (2/6)
        //4  Green   (1/2)

        System.out.println(teamList);

        sender.sendMessage(teamList.toString());
    }

    private static void teamClearCommand(final CommandSender sender, String[] args)
    {
        // /team clear [username]

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
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

        User user = FracturedCore.getUserManager().getUser(target.getUniqueId());

        if (user.getTeam() == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TARGET);
            return;
        }

        TeamManager.removeTeam(target, user);
    }

    /**
     * Tries to get the requested team
     * @param s some identifier that (should) connects to a team
     * @return Doesn't guarentee that the team is not null.
     */
    private static Team getTeam(String s)
    {
        // Get by name first
        Team rax = TeamCache.getTeam(s);

        // If there is no team by that name, try the id
        if (rax == null)
        {
            int i;

            try
            {
                i = Integer.parseInt(s);
            } catch (NumberFormatException e)
            {
                // id is invalid, so no team could be found
                return null;
            }

            // Input is an int, we can try by the team's id
            rax = TeamCache.getTeam(i);
        }

        return rax;
    }

    private static void teamSetCommand(final CommandSender sender, String[] args)
    {
        // /team set [user] [team | id]
        // console allowed,

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
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

        User user = FracturedCore.getUserManager().getUser(target.getUniqueId());

        if (user.getTeam() != null)
        {
            sender.sendMessage(Messages.COMMAND_TEAM_SET_TARGET_ALREADY_IN_TEAM);
            return;
        }

        Team team = getTeam(args[2]);

        if (team == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
            return;
        }

        // Team is not null
        TeamManager.addTeam(target, team);
    }

    private static void teamForceSetCommand(final CommandSender sender, String[] args)
    {
        // /team set [user] [team | id]
        // console allowed,

        if (!sender.hasPermission(Permissions.COMMAND_TEAM_ADMIN))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
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

        User user = FracturedCore.getUserManager().getUser(target.getUniqueId());

        Team team = getTeam(args[2]);

        if (team == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
            return;
        }

        TeamManager.forceSetTeam(target, user, team);
    }

    private static void teamJoinCommand(final CommandSender sender, String[] args)
    {
        // /team join [team | id]
        // anyone can execute this except for console

        if (!(sender instanceof Player))
        {
            sender.sendMessage(Messages.CONSOLE_BLOCKED);
            return;
        }

        Player player = (Player) sender;
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());

        if (user.getTeam() != null)
        {
            sender.sendMessage(Messages.COMMAND_TEAM_ALREADY_IN_TEAM);
            return;
        }

        Team team = getTeam(args[1]);
        if (team == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
            return;
        }

        TeamManager.addTeam(player, team);
    }

    private static void teamForceJoinCommand(final CommandSender sender, String[] args)
    {
        // /team forcejoin [team | id]
        // anyone can execute this except for console

        if (!(sender instanceof Player))
        {
            sender.sendMessage(Messages.CONSOLE_BLOCKED);
            return;
        }

        Player player = (Player) sender;
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());

        Team team = getTeam(args[1]);
        if (team == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TEAM);
            return;
        }

        TeamManager.forceSetTeam(player, user, team);
    }

    private static void teamMenuCommand(final CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.CONSOLE_BLOCKED);
            return;
        }

        Player player = (Player) sender;
        User user = FracturedCore.getUserManager().getUser(player.getUniqueId());

        if (user.getTeam() != null) {
            sender.sendMessage(Messages.COMMAND_TEAM_ALREADY_IN_TEAM);
            return;
        }

        TeamCache.openMenu(player);
    }
}
