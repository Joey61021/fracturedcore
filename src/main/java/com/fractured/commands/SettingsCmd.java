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

public class SettingsCmd implements CommandExecutor {

    public static ArrayList<Player> build = new ArrayList<>();

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
            SettingsManager.displayGUI(player);
            return false;
        }

        for (Settings setting : Settings.values()) {
            if (args[1].equalsIgnoreCase(setting.getSettingsValue())) {
                SettingsManager.toggleSetting(player, setting);
                return false;
            }
        }

        player.sendMessage(Utils.Color(Message.INVALID_ARG.getMessage()));
        return false;
    }
}
