package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.enums.Settings;
import com.fractured.managers.SettingsManager;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SetLocation implements CommandExecutor {

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

        if (args.length < 1) {
            player.sendMessage(Utils.Color(Message.INVALID_ARG.getMessage()));
            return false;
        }

        if (!SettingsManager.setLocation(args[0], player.getLocation())) {
            player.sendMessage(Utils.Color(Message.CMD_SET_LOCATION_INVALID.getMessage()));
            return false;
        }

        player.sendMessage(Utils.Color(Message.CMD_SET_LOCATION_SET.getMessage()).replace("%location%", args[0]));
        return false;
    }
}
