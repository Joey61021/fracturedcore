package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Utils.Color(Message.CONSOLE_BLOCKED.getMessage()));
            return false;
        }

        Player player = (Player) sender;
        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            player.sendMessage(Utils.Color(Message.CMD_SPAWN_NO_TEAM.getMessage()));
            return false;
        }

        if (!LocationManager.isInRegion(player.getLocation(), team.getPos1(), team.getPos2())) {
            player.sendMessage(Utils.Color(Message.CMD_SPAWN_NOT_IN_REGION.getMessage()));
            return false;
        }

        player.teleport(team.getSpawn());
        player.sendMessage(Utils.Color(Message.CMD_SPAWN_TELEPORTED.getMessage().replace("%team%", team.getColor() + team.getName())));
        return false;
    }
}
