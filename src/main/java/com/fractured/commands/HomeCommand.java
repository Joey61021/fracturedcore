package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.events.world.WorldManager;
import com.fractured.team.Claim;
import com.fractured.team.ClaimManager;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class HomeCommand
{
    private HomeCommand()
    {
    }

    public static boolean home(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        Player player = (Player) sender;
        Team team = UserManager.getUser(player.getUniqueId()).getTeam();

        if (team == null) {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_TEAM_BLOCKED));
            return false;
        }

        Claim claim = ClaimManager.getClaim(player.getLocation());
        if (player.getLocation().getWorld() != WorldManager.getSpawn().getWorld() || claim == null || claim.getTeam() == null || claim.getTeam() != team)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.ENEMY_TEAM_BLOCKED));
            return false;
        }

        player.sendMessage(FracturedCore.getMessages().get(Messages.TELEPORTING));
        player.teleport(player.getRespawnLocation() == null ? team.spawn() : player.getRespawnLocation());
        return true;
    }
}
