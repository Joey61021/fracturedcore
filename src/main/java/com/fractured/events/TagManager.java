package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TagManager implements Listener
{

    public static Set<String> activeTags = new HashSet<>();

    public static void init()
    {
        Config config = FracturedCore.getFracturedConfig();
        for (String tag : config.getConfigurationSection("tags.").getKeys(false))
        {
            String tagString = config.getString("tags." + tag);

            activeTags.add(tagString);
            System.out.println("[FracturedCore] Registered new tag: " + tag);
        }
    }

    public static void openGUI(Player player, Player target)
    {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, "Available tags");

        User user = UserManager.getUser(player);
        if (user == null)
        {
            return;
        }

        user.setSelectedPlayer(target.getUniqueId());

        for (String string : activeTags)
        {
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();

            if (meta != null)
            {
                ArrayList<String> lore = new ArrayList<>();
                meta.setDisplayName(Utils.color(string));
                lore.add(Utils.color("&7Click to assign this tag to " + target.getName() + "!"));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            inv.addItem(item);
        }

        player.openInventory(inv);
    }

    public static void assignTag(Player player, String tag)
    {
        User user = UserManager.getUser(player);
        if (user == null)
        {
            return;
        }

        user.setTag(tag);
    }

    @EventHandler
    public static void onClick(InventoryClickEvent event)
    {
        Inventory inv = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (inv == null || item == null || item.getType().equals(Material.AIR) || !event.getView().getTitle().equalsIgnoreCase("Available tags") || !player.hasPermission(Permissions.COMMAND_TAG))
        {
            return;
        }

        event.setCancelled(true);

        for (String tags : activeTags)
        {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName())
            {
                if (meta.getDisplayName().equalsIgnoreCase(Utils.color(tags)))
                {
                    User user = UserManager.getUser(player);
                    if (user == null || user.getSelectedPlayer() == null)
                    {
                        return;
                    }

                    Player target = Bukkit.getPlayer(user.getSelectedPlayer());
                    if (target == null)
                    {
                        player.closeInventory();
                        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TAG_PLAYER_LOGGED));
                        return;
                    }

                    assignTag(target, tags);
                    player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TAG_ASSIGNED).replace("%player%", target.getDisplayName()).replace("%tag%", Utils.color(tags)));
                    return;
                }
            }
        }
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event)
    {
        Config tags = FracturedCore.getTags();
        Player player = event.getPlayer();
        User user = UserManager.getUser(player);
        if (user == null)
        {
            return;
        }

        if (tags.isSet(player.getUniqueId().toString()))
        {
            user.setTag(tags.getString(player.getUniqueId().toString()));
        }
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event)
    {
        Config tags = FracturedCore.getTags();
        Player player = event.getPlayer();
        User user = UserManager.getUser(player);

        if (user == null)
        {
            return;
        }

        if (user.getTag() != null)
        {
            tags.set(player.getUniqueId().toString(), user.getTag());
            tags.save();
        }
    }
}
