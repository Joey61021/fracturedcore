package com.fractured.commands;

import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.TeamManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, Message.CONSOLE_BLOCKED);
            return false;
        }

        Player player = (Player) sender;
        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            MessageManager.sendMessage(player, Message.CMD_SPAWN_NO_TEAM);
            return false;
        }

        if (!LocationManager.isInRegion(player.getLocation(), team.getPos1(), team.getPos2())) {
            MessageManager.sendMessage(player, Message.CMD_SPAWN_NOT_IN_REGION);
            return false;
        }

        player.teleport(team.getSpawn());
        MessageManager.sendMessage(player, Message.CMD_SPAWN_TELEPORTED, (s) -> s.replace("%team%", team.getColor() + team.getName()));
        return false;
    }
}
