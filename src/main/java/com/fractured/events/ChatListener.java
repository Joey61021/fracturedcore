package com.fractured.events;

import com.fractured.enums.Teams;
import com.fractured.managers.TeamManager;
import com.fractured.managers.message.Message;
import com.fractured.managers.message.MessageManager;
import com.fractured.utilities.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (TeamManager.toggled.contains(player)) {
            event.setCancelled(true);
            if (!TeamManager.sendTeamMessage(player, event.getMessage())) {
                TeamManager.toggleTeamChat(player);
                MessageManager.sendMessage(player, Message.CMD_TC_NOT_IN_TEAM);
            }
            return;
        }
        Teams team = TeamManager.getTeam(player);
        event.setFormat((team != null ? team.getColor() : ChatColor.GRAY) + Utils.color("%1$s: &f%2$s"));
    }
}
