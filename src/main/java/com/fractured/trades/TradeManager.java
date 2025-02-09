package com.fractured.trades;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TradeManager implements Listener {

    public static Set<TradeRequest> requests = new HashSet<>();
    public static Map<Trade, Trade> trades = new HashMap<>();

    public static int COOLDOWN = 60000; // One minute

    public static TradeRequest getActiveRequest(Player player)
    {
        TradeRequest tradeRequest = null;
        for (TradeRequest request : requests)
        {
            if (!request.getRequester().equals(player.getUniqueId()))
            {
                continue;
            }

            tradeRequest = request;
        }

        return tradeRequest;
    }

    public static int generateId() {
        Set<Integer> ids = new HashSet<>();
        trades.values().forEach(trade -> ids.add(trade.getId()));

        for (int i = 0; i < 99; i++) {
            if (!ids.contains(i)) {
                return i;
            }
        }

        return -1; // Unlikely, unless all IDs are used
    }

    public static void startTrade(Player player, Player target)
    {
        TradeRequest activeRequest = getActiveRequest(player);
        if (activeRequest != null && System.currentTimeMillis() - activeRequest.getTimestamp() < COOLDOWN)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TRADE_ALREADY_ACTIVE).replace("%time%", (COOLDOWN - (System.currentTimeMillis() - activeRequest.getTimestamp())) / 1000 + "s"));
            return;
        }

        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TRADE_SENT).replace("%player%", target.getDisplayName()));
        target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TRADE_RECEIVED).replace("%player%", player.getDisplayName()));

        requests.removeIf(request -> request.getRequester().equals(player.getUniqueId()));
        requests.add(new TradeRequest(player.getUniqueId(), target.getUniqueId()));

        Inventory inv = openInventory(player, target);

        int id = generateId();
        trades.put(new Trade(id, player.getUniqueId(), inv), new Trade(id, target.getUniqueId(), inv));
    }

    public static Inventory openInventory(Player player, Player target)
    {
        Inventory inv = Bukkit.createInventory(null, 6*9, player.getName() + " / " + target.getName());

        ItemStack divider = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta dividerMeta = divider.getItemMeta();

        if (dividerMeta != null)
        {
            dividerMeta.setDisplayName(" ");
            divider.setItemMeta(dividerMeta);
        }

        for (int i = 0; i < 6; i++)
        {
            inv.setItem(4 + (i * 9), divider);
        }

        player.openInventory(inv);
        target.openInventory(inv);

        return inv;
    }

    public static Trade getTrade(Player player)
    {
        for (Trade trade : trades.values())
        {
            if (trade.getUUID().equals(player.getUniqueId()))
            {
                return trade;
            }
        }

        return null;
    }

    public static void cancelTrade(Trade trade)
    {
        for (Trade vals : trades.values())
        {
            if (vals.getId() == trade.getId())
            {
                Player player = Bukkit.getPlayer(trade.getUUID());
                trades.remove(vals);

                if (player == null)
                {
                    continue;
                }

                player.closeInventory();
            }
        }
    }

    public static void onClose(InventoryCloseEvent event)
    {
        Player player = (Player) event.getPlayer();
        Trade trade = getTrade(player);

        if (trade == null || event.getInventory() != player.getOpenInventory().getTopInventory())
        {
            return;
        }

        cancelTrade(trade);
    }

    public static void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        Trade trade = getTrade(player);

        if (trade == null)
        {
            return;
        }

        cancelTrade(trade);
    }
}
