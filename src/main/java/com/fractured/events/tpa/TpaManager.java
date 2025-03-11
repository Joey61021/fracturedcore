package com.fractured.events.tpa;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public class TpaManager implements Listener
{

    public static Set<TpaRequest> requests = new HashSet<>();
    private static final long COOLDOWN = 60000;

    private static void wait(TpaRequest request, Player player) {
        FracturedCore.runDelay(() -> {
            if (requests.contains(request))
            {
                requests.remove(request);
                player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_EXPIRED));
            }
        }, 20L * 60); //20 = 1 second
    }

    public static int getCooldown(Player player)
    {
        for (TpaRequest request : requests)
        {
            if (request.getRequester().equals(player.getUniqueId()))
            {
                long lapsed = System.currentTimeMillis() - request.getTimestamp();
                if (lapsed >= COOLDOWN)
                {
                    requests.remove(request);
                    continue; // ignore if cooldown has lapsed
                }

                return (int) (COOLDOWN - lapsed) / 1000;
            }
        }
        return 0;
    }

    public static void addRequest(boolean tpaHere, Player requester, Player requested)
    {
        TpaRequest request = new TpaRequest(tpaHere, requester.getUniqueId(), requested.getUniqueId());
        requests.add(request);
        wait(request, requester);
    }

    public static Set<TpaRequest> getRequests()
    {
        return requests;
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        for (TpaRequest request : requests)
        {
            if (request.getRequester().equals(player.getUniqueId()))
            {
                requests.remove(request);
                continue; // cannot be both
            }

            if (request.getRequested().equals(player.getUniqueId()))
            {
                Player target = Bukkit.getPlayer(request.getRequester());
                if (target != null)
                {
                    target.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_TPA_LOGGED).replace("%player%", player.getName()));
                }

                requests.remove(request);
            }
        }

    }
}
