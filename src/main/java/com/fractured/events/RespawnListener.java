package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.commands.SettingsCommand;
import com.fractured.team.Team;
import com.fractured.team.upgrades.UpgradeCache;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.ConfigKeys;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player.getUniqueId());
        Team team = user.getTeam();

        if (team == null) {
            return;
        }

        if (!event.isBedSpawn()) {
            event.setRespawnLocation(team.spawn());
        }

        player.getInventory().setHelmet(team.helmet());

        if (FracturedCore.getFracturedConfig().get(ConfigKeys.STARTER_ITEMS))
        {
            player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
            player.getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
            player.getInventory().addItem(new ItemStack(Material.WOODEN_PICKAXE));
        }
    }
}
