package com.fractured.events.world;

import com.fractured.commands.BuildCmd;
import com.fractured.enums.AlertReason;
import com.fractured.enums.Message;
import com.fractured.enums.Teams;
import com.fractured.managers.LocationManager;
import com.fractured.managers.RegionManager;
import com.fractured.managers.TeamManager;
import com.fractured.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.getGameMode() == GameMode.CREATIVE) {
            if (!BuildCmd.build.contains(player)) {
                player.sendMessage(Utils.Color(Message.UNABLE_TO_BUILD.getMessage()));
                event.setCancelled(true);
                return;
            }

            if (player.getItemInHand().getType() == Material.BLAZE_ROD && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("region wand")) {
                event.setCancelled(true);
                RegionManager.selectRegion(false, block.getLocation());
                player.sendMessage(Utils.Color(Message.REGION_SELECTED.getMessage().replace("%pos%", "Pos2").replace("%team%", RegionManager.selectedTeam.getColor() + RegionManager.selectedTeam.getName())));
            }
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || !player.getWorld().getName().equalsIgnoreCase("world")) {
            return;
        }

        Teams team = TeamManager.getTeam(player);

        if (team == null) {
            event.setCancelled(true);
            return;
        }

        if (!LocationManager.isInRegion(block.getLocation(), team.getPos1(), team.getPos2())) {
            Teams enemyTeam = LocationManager.getEnemyTeam(team, block.getLocation());
            if (enemyTeam == null) {
                event.setCancelled(true);
                return;
            }

            if (enemyTeam.getPlayers().size() < 1) {
                event.setCancelled(true);
                player.sendMessage(Utils.Color(Message.REGION_TEAM_OFFLINE.getMessage()));
                return;
            }

            TeamManager.alertTeam(player, enemyTeam, block.getLocation(), AlertReason.BLOCK_BREAK);
        }


        if (isOre(block.getType())) {
            Set<Block> countedBlocks = new HashSet<>();
            countOreVein(block, countedBlocks);

            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.hasPermission("fractured.admin")) {
                    players.sendMessage(Utils.Color("&c&l[XRAY] " + player.getDisplayName() + " &fhas broken a vein of &b" + countedBlocks.size() + " " + block.getType().name() + " &f!"));
                }
            }
        }
    }

    private boolean isOre(Material material) {
        return material == Material.IRON_ORE || material == Material.GOLD_ORE ||
                material == Material.DIAMOND_ORE || material == Material.COAL_ORE ||
                material == Material.EMERALD_ORE || material == Material.REDSTONE_ORE ||
                material == Material.LAPIS_ORE || material == Material.NETHER_QUARTZ_ORE ||
                material == Material.ANCIENT_DEBRIS;
    }

    private void countOreVein(Block block, Set<Block> countedBlocks) {
        if (!isOre(block.getType()) || countedBlocks.contains(block)) {
            return;
        }

        countedBlocks.add(block);

        for (BlockFace face : BlockFace.values()) {
            Block relative = block.getRelative(face);
            if (isOre(relative.getType()) && !countedBlocks.contains(relative)) {
                countOreVein(relative, countedBlocks);
            }
        }
    }
}
