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

public class ShardCmd implements CommandExecutor {

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

        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.Color("&bFractured Shard"));
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.Color("&7Insert this shard into the centre beacon to activate"));
        meta.setLore(lore);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);

        player.sendMessage(Utils.Color("&eYou have been given the a &bFractured Shard"));
        return false;
    }
}
