package com.fractured.events.inventory;

import com.fractured.team.TeamCache;
import com.fractured.team.TeamManager;
import com.fractured.team.Team;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryClickListener implements Listener
{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Entity player = event.getWhoClicked();
        if (!event.getView().getTitle().toLowerCase().contains("select team"))
        {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null)
        {
            for (Team team : TeamCache.teams())
            {
                if (clickedItem.getType() == team.material())
                {
                    TeamManager.addTeam((Player) player, team);
                    player.teleport(team.spawn());
                    event.getView().close();
                }
            }
        }
    }
}
