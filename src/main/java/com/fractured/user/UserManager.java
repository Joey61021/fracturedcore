package com.fractured.user;

import com.fractured.FracturedCore;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Each {@link Player} has associated a {@link com.fractured.user.User} object.
 * This is to store additional data about the Player. This User object is
 * guaranteed to exist if the Player is online.
 */
public final class UserManager implements Listener
{
    private static final Map<UUID, User> users;

    static
    {
        users = new ConcurrentHashMap<>(); // concurrent for async reading and writing (onPreLogin for example)
    }

    public static User getUser(UUID uid)
    {
        return users.get(uid);
    }

    public static User getUser(HumanEntity player)
    {
        return getUser(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST) // run first
    public static void onPreLogin(AsyncPlayerPreLoginEvent event)
    {
        UUID uid = event.getUniqueId();
        User user = new User(uid);

        // load the user async (The event is async)
        FracturedCore.getStorage().load(user);

        users.put(uid, user);
    }

    @EventHandler(priority = EventPriority.HIGHEST) // run last
    public static void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        User user = users.remove(player.getUniqueId());

        // Shouldn't ever be null, but I hate my life
        if (user != null)
        {
            // save the user async
            FracturedCore.runAsync(() -> FracturedCore.getStorage().save(user));
        }
    }
}
