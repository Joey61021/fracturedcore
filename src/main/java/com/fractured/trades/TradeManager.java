package com.fractured.trades;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.entity.Player;

import java.util.*;

public class TradeManager {

    public static Set<TradeRequest> requests = new HashSet<>();

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

    public static void startTrade(Player player, Player target)
    {
        TradeRequest activeRequest = getActiveRequest(player);
        if (activeRequest != null && System.currentTimeMillis() - activeRequest.getTimestamp() < COOLDOWN)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TRADE_ALREADY_ACTIVE).replace("%time%", String.valueOf(System.currentTimeMillis() - activeRequest.getTimestamp() / 1000)));
            return;
        }

        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TRADE_SENT).replace("%player%", target.getDisplayName()));
        target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TRADE_RECEIVED).replace("%player%", player.getDisplayName()));

        requests.removeIf(request -> request.getRequester().equals(player.getUniqueId()));
        requests.add(new TradeRequest(player.getUniqueId(), target.getUniqueId()));
    }
}
