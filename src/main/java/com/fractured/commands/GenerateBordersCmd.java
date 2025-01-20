package com.fractured.commands;

import com.fractured.enums.Message;
import com.fractured.managers.LocationManager;
import com.fractured.utilities.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class GenerateBordersCmd implements CommandExecutor {

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

        if (args.length < 1 || !Objects.equals(args[0], "confirm")) {
            player.sendMessage(Utils.Color(Message.CMD_GENERATE_BORDER_CONFIRM.getMessage()));
            return false;
        }

        player.sendMessage(Utils.Color(Message.CMD_GENERATE_BORDER_GENERATING.getMessage()));

        int size = 500;
        int x = (int) player.getLocation().getX();
        int y = (int) player.getLocation().getY()-1;
        int z = (int) player.getLocation().getZ();

        for (int a = 0; a < (y - LocationManager.MIN_HEIGHT); a++) {
            LocationManager.world.getBlockAt(x, y - a, z).setType(Material.BEDROCK);

            for (int b = 0; b < size; b++) {
                LocationManager.world.getBlockAt(x + 1 + b, y - a, z - 1).setType(Material.RED_CONCRETE);
                LocationManager.world.getBlockAt(x + 1 + b, y - a, z).setType(Material.BEDROCK);
                LocationManager.world.getBlockAt(x + 1 + b, y - a, z + 1).setType(Material.BLUE_CONCRETE);
            }

            for (int b = 0; b < size; b++) {
                LocationManager.world.getBlockAt(x - 1, y - a, z + 1 + b).setType(Material.YELLOW_CONCRETE);
                LocationManager.world.getBlockAt(x, y - a, z + 1 + b).setType(Material.BEDROCK);
                LocationManager.world.getBlockAt(x + 1, y - a, z + 1 + b).setType(Material.BLUE_CONCRETE);
            }

            for (int b = 0; b < size; b++) {
                LocationManager.world.getBlockAt(x - 1 - b, y - a, z - 1).setType(Material.LIME_CONCRETE);
                LocationManager.world.getBlockAt(x - 1 - b, y - a, z).setType(Material.BEDROCK);
                LocationManager.world.getBlockAt(x - 1 - b, y - a, z + 1).setType(Material.YELLOW_CONCRETE);
            }

            for (int b = 0; b < size; b++) {
                LocationManager.world.getBlockAt(x - 1, y - a, z - 1 - b).setType(Material.LIME_CONCRETE);
                LocationManager.world.getBlockAt(x, y - a, z - 1 - b).setType(Material.BEDROCK);
                LocationManager.world.getBlockAt(x + 1, y - a, z - 1 - b).setType(Material.RED_CONCRETE);
            }
        }

        LocationManager.world.getBlockAt(x, y, z).setType(Material.WHITE_STAINED_GLASS);
        LocationManager.world.getBlockAt(x, y-1, z).setType(Material.BEACON);

        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                LocationManager.world.getBlockAt(x-1+a, y-2, z-1+b).setType(Material.IRON_BLOCK);
            }
        }

        player.sendMessage(Utils.Color(Message.CMD_GENERATE_BORDER_COMPLETE.getMessage()));
        return false;
    }
}
