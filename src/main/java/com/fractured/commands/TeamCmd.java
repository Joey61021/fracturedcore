package com.fractured.commands;

import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.team.TeamCache;
import com.fractured.team.TeamManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        Staff command that allows you to set another player's team (mainly for bug fixes or any player goofs)
        /team set Pvbble [name | id | (null | -1)]

        Staff command that puts you in a team, removing you from the team you're already in
        /team forcejoin [name | id]
         */
        subCommands.register("set", TeamCmd::teamSetCommand, "s");
        subCommands.register("join", TeamCmd::teamSetCommand, "j");
        subCommands.register("forcejoin", TeamCmd::teamSetCommand, "fj");
        subCommands.register("menu", TeamCmd::teamSetCommand, "m");
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!subCommands.dispatch(sender, args))
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

        //TeamCache.getTeam()
    }
}
