package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatListener implements Listener
{

    private static final long COOLDOWN = 2000;

    @EventHandler
    public static void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (!player.hasPermission(Permissions.CHAT_BYPASS))
        {
            if (System.currentTimeMillis() - user.getLastMessageTimestamp() < COOLDOWN)
            {
                event.setCancelled(true);
                player.sendMessage(FracturedCore.getMessages().get(Messages.CHAT_COOLDOWN).replace("%time%", String.valueOf((COOLDOWN - (System.currentTimeMillis() - user.getLastMessageTimestamp())) / 1000)));
                return;
            }

            user.setLastMessageTimestamp();
        }

        if (user.isInTeamChat())
        {
            // team chat
            event.setCancelled(true);
            team.alert(Utils.color(team.color() + "&l[" + team.getName().charAt(0) + "] &r" + player.getName() + ": &f" + event.getMessage()));
        } else
        {
            // public chat
            event.setFormat((team != null ? team.color() : ChatColor.GRAY) + Utils.color("%1$s&f: %2$s"));
        }
    }
}
