package com.fractured.commands.team;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.team.TeamManager;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class TeamChatCommand
{
    private TeamChatCommand()
    {
    }

    public static boolean teamchat(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        Team team = UserManager.getUser(player).getTeam();

        if (team == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_TEAM_BLOCKED));
            return true;
        }

        if (args.length == 0)
        {
            TeamManager.toggleTeamChat((Player) sender);
            return true;
        }

        StringBuilder sb = new StringBuilder();
        Arrays.stream(args).toList().forEach(s -> sb.append(s).append(" "));
        String message = sb.toString();
        team.alert(Utils.color(team.color() + "&l[" + team.getName().charAt(0) + "] &r" + player.getName() + ": &f" + message));
        return true;
    }
}
