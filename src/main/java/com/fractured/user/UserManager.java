package com.fractured.user;

import com.fractured.FracturedCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager implements Listener
{
    private final Map<UUID, User> users;

    public UserManager()
    {
        users = new HashMap<>();
    }

    public User getUser(UUID uid)
    {
        return users.get(uid);
    }

    @EventHandler(priority = EventPriority.LOWEST) // run first
    public void onPreLogin(AsyncPlayerPreLoginEvent event)
    {
        UUID uid = event.getUniqueId();
        User user = new User(uid);

        // load the user async (The event is async)
        FracturedCore.getStorage().load(user);

        users.put(uid, user);
    }

    @EventHandler(priority = EventPriority.HIGHEST) // run last
    public void onQuit(PlayerQuitEvent event)
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
