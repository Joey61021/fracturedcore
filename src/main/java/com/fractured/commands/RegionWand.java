package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.managers.RegionManager;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RegionWand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Utils.Color(Message.CONSOLE_BLOCKED.getMessage()));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("fractured.admin")) {
            player.sendMessage(Utils.Color(Message.NO_PERMISSION.getMessage()));
            return false;
        }

        player.getInventory().addItem(RegionManager.getWand(RegionManager.selectedTeam));
        return false;
    }
}
