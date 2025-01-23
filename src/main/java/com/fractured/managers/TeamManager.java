package com.fractured.managers;

import com.fractured.FracturedCore;
import com.fractured.enums.AlertReason;
import com.fractured.enums.Settings;
import com.fractured.enums.Teams;
import com.fractured.enums.Upgrades;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import com.fractured.utilities.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Map;

public class TeamManager {

    public static ArrayList<Player> toggled = new ArrayList<>();


    public static void displayGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, Utils.color("&8Select Team"));
        player.openInventory(inv);

        for (Teams team : Teams.values()) {
            ItemStack item = team.getItem();

            // Meta data
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(team.getColor() + team.getName() + " team");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Utils.color("&7Online: " + team.getPlayers().size()));
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

    public static ItemStack getHelmet(Teams team) {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        Map<ChatColor, Color> colorMap = Map.of(
                ChatColor.RED, Color.RED,
                ChatColor.BLUE, Color.BLUE,
                ChatColor.GREEN, Color.GREEN
        );

        ChatColor color = team.getColor();
        meta.setColor(colorMap.getOrDefault(color, Color.YELLOW));

        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, FracturedCore.getSettings.getInt("upgrades." + team.getName().toLowerCase() + "." + Upgrades.HELMET_PROTECTION.getUpgradeValue(), 1), true);
        meta.setUnbreakable(true);
        meta.setDisplayName(Utils.color(team.getColor() + team.getName() + " team"));
        item.setItemMeta(meta);
        return item;
    }

    public static void establishTeam(Player player, Teams team) {
        FracturedCore.getDatabase.set("teams." + player.getUniqueId(), team.toString());
        FracturedCore.getDatabase.save();

        if (SettingsManager.isToggled(Settings.STARTER_ITEMS)) {
            player.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
            player.getInventory().setItem(1, new ItemStack(Material.WOODEN_AXE));
            player.getInventory().setItem(2, new ItemStack(Material.WOODEN_PICKAXE));
        }

        player.getInventory().setHelmet(getHelmet(team));

        team.getPlayers().add(player);
        player.teleport(team.getSpawn());
        player.setBedSpawnLocation(team.getSpawn());
        player.setPlayerListName(team.getColor() + player.getName());
        player.setPlayerListFooter(Utils.color("&7Your team: " + team.getColor() + team.getName() + " team"));

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
            players.sendMessage(Utils.color(team.getColor() + "&l[" + team.getName().charAt(0) + "] &r" + player.getName() + ": &f" + message));
        }
        return true;
    }

    public static void alertTeam(Player player, Teams team, Location location, AlertReason alertReason) {
        MessageManager.sendMessage(player, Message.REGION_TEAM_ALERTED, (s) -> s.replace("%team%", team.getName().toLowerCase()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60 * 20, 0, false, false));

        if (team.getPlayers().size() > 0) {
            for (Player players : team.getPlayers()) {
                MessageManager.sendMessage(players, alertReason.getMessage(),
                                (s) -> s.replace("%player%", player.getName())
                                .replace("%team%", team.getName())
                                .replace("%locx%", String.valueOf(Math.round(location.getX())))
                                .replace("%locy%", String.valueOf(Math.round(location.getY())))
                                .replace("%locz%", String.valueOf(Math.round(location.getZ()))));
            }
        }
    }

    public static Teams applyTeam(Player player, String pooledTeam) {
        Teams team = Teams.valueOf(pooledTeam);
        team.getPlayers().add(player);

        player.setPlayerListName(team.getColor() + player.getName());
        player.setDisplayName(Utils.color(team.getColor() + player.getDisplayName()));
        player.setPlayerListFooter(Utils.color("&7Your team: " + team.getColor() + team.getName() + " team"));
        player.getInventory().setHelmet(TeamManager.getHelmet(team));
        return team;
    }
}
