package com.fractured.events;

import com.fractured.cevents.pantheon.DialogueManager;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class EventDispatcher implements Listener
{
    public EventDispatcher()
    {

    }

    @EventHandler
    public static void onInteractAtEntity(PlayerInteractAtEntityEvent event)
    {
        User user = UserManager.getUser(event.getPlayer().getUniqueId());

        DialogueManager.onInteractAtEntity(user, event);
    }
}
