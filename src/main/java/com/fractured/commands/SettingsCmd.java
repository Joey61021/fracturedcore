package com.fractured.commands;

import com.fractured.enums.Settings;
import com.fractured.managers.SettingsManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, Message.CONSOLE_BLOCKED);
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("fractured.admin")) {
            MessageManager.sendMessage(player, Message.NO_PERMISSION);
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

        MessageManager.sendMessage(player, Message.INVALID_ARG);
        return false;
    }
}
