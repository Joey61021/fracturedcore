package com.fractured.commands.team;

import com.fractured.FracturedCore;
import com.fractured.events.WorldManager;
import com.fractured.team.Claim;
import com.fractured.team.ClaimManager;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetHomeCommand
{
    private SetHomeCommand()
    {
    }

    public static boolean sethome(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        Team team = UserManager.getUser(player.getUniqueId()).getTeam();

        if (team == null) {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_TEAM_BLOCKED));
            return false;
        }

        Claim claim = ClaimManager.getClaim(player.getLocation());
        if (player.getLocation().getWorld() != WorldManager.SPAWN.getWorld() || claim == null || claim.getTeam() == null || claim.getTeam() != team)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_HOME_INVALID_REGION));
            return false;
        }

        player.setRespawnLocation(player.getLocation());
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_HOME_SET));
        return true;
    }
}
