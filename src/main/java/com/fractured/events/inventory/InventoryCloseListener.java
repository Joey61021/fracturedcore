package com.fractured.events.inventory;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener
{

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        Player player = (Player) event.getPlayer();

        // todo migrate to MenuManager registry
        if (event.getView().getTitle().toLowerCase().contains("select team"))
        {
            Team team = UserManager.getUser(player).getTeam();
            if (team != null)
            {
                return;
            }

            player.sendMessage(FracturedCore.getMessages().get(Messages.TEAM_INVENTORY_CLOSED));
        }
    }
}
