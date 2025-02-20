package com.fractured.util;

import com.fractured.team.*;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion
{

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor(){
        return "tex";
    }

    @Override
    public @NotNull String getIdentifier(){
        return "fractured";
    }

    @Override
    public @NotNull String getVersion(){
        return "1.0-SNAPSHOT";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null)
        {
            return null;
        }

        User user = UserManager.getUser(player);
        if (user == null)
        {
            return Utils.color("&cInvalid state");
        }

        Team team = user.getTeam();
        Claim claim = ClaimManager.getClaim(player.getLocation());

        return switch (identifier) {
            case "team" -> Utils.color(team == null ? "<gray>None" : convertColor(team.color()) + team.getName());
            case "region" -> Utils.color(claim == null ? "<gray>None" : convertColor(claim.getTeam().color()) + claim.getTeam().getName());
            default -> null;
        };
    }

    public static String convertColor(ChatColor color) {
        return switch (color) {
            case RED -> "<red>";
            case BLUE -> "<blue>";
            case GREEN -> "<green>";
            case YELLOW -> "<yellow>";
            default -> "<gray>";
        };
    }
}
