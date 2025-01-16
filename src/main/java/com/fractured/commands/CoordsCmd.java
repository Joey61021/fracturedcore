package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsCmd implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(Utils.Color(Message.CONSOLE_BLOCKED.getMessage()));
            return false;
        }

        Player player = (Player) sender;
        player.chat(Utils.Color("My coords are: X: %locx%, Y: %locy%, Z: %locz%!")
                .replace("%locx%", "" + Math.round(player.getLocation().getX()))
                .replace("%locy%", "" + Math.round(player.getLocation().getY()))
                .replace("%locz%", "" + Math.round(player.getLocation().getZ())));
        return false;
    }
}
