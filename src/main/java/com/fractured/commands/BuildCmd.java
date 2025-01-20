package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.utilities.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class BuildCmd implements CommandExecutor {

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

        if (build.contains(player)) {
            build.remove(player);
            player.sendMessage(Utils.Color(Message.CMD_BUILD_TOGGLE_OFF.getMessage()));
            return false;
        }
        build.add(player);
        player.sendMessage(Utils.Color(Message.CMD_BUILD_TOGGLE_ON.getMessage()));
        return false;
    }
}
