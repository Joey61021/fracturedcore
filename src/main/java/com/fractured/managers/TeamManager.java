package com.fractured.managers;

import com.fractured.FracturedCore;
import com.fractured.enums.AlertReason;
import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TeamManager {

    public static ArrayList<Player> toggled = new ArrayList<>();

    public static void displayGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, Utils.Color("&7Select Team"));
        player.openInventory(inv);

        for (Teams team : Teams.values()) {
            ItemStack item = team.getItem();

            // Meta data
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(team.getColor() + team.getName() + " team");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Utils.Color("&7Online: " + team.getPlayers().size()));
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.setItem(team.getSlot(), item);
        }
    }

    public static Teams getTeam(Player player) {
        for (Teams team : Teams.values()) {
            for (Player players : team.getPlayers()) {
                if (player == players) {
                    return team;
                }
            }
        }
        return null;
    }

    public static void establishTeam(Player player, Teams team) {
        FracturedCore.getDatabase.set("teams." + player.getUniqueId(), team.toString());
        FracturedCore.getDatabase.save();

        team.getPlayers().add(player);
        player.teleport(team.getSpawn());
        player.setBedSpawnLocation(team.getSpawn());
        player.setPlayerListName(team.getColor() + player.getName());
        player.setPlayerListFooter(Utils.Color("&7Your team: " + team.getColor() + team.getName() + " team"));

        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(team.getColor() + player.getName() + " has joined the " + team.getName() + " team!");
        }
    }

    public static boolean toggleTeamChat(Player player) {
        if (!toggled.contains(player)) {
            toggled.add(player);
            return true;
        } else {
            toggled.remove(player);
            return false;
        }
    }

    public static boolean sendTeamMessage(Player player, String message) {
        Teams team = getTeam(player);
        if (team == null) {
            return false;
        }

        for (Player players : team.getPlayers()) {
            players.sendMessage(Utils.Color(team.getColor() + "&l[" + team.getName().charAt(0) + "] &r" + player.getName() + ": &f" + message));
        }
        return true;
    }

    public static void alertTeam(Player player, Teams team, Location location, AlertReason alertReason) {
        player.sendMessage(Utils.Color(Message.REGION_TEAM_ALERTED.getMessage().replace("%team%", team.getName())));

        if (team.getPlayers().size() > 0) {
            for (Player players : team.getPlayers()) {
                players.sendMessage(Utils.Color(alertReason.getMessage().replace("%player%", player.getName()).replace("%team%", team.getName())
                        .replace("%locx%", String.valueOf(Math.round(location.getX()))))
                        .replace("%locy%", String.valueOf(Math.round(location.getY())))
                        .replace("%locz%", String.valueOf(Math.round(location.getZ()))));
            }
        }
    }

    public static Teams applyTeam(Player player, String pooledTeam) {
        Teams team = Teams.valueOf(pooledTeam);
        team.getPlayers().add(player);
        player.setPlayerListName(team.getColor() + player.getName());
        player.setPlayerListFooter(Utils.Color("&7Your team: " + team.getColor() + team.getName() + " team"));
        return team;
    }
}
