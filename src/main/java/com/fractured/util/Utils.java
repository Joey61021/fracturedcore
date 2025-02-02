package com.fractured.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public final class Utils
{
    private Utils()
    {

    }

    public static String color(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static Material getGlassFrom(char color)
    {
        return switch (color)
        {
            case '0' -> Material.BLACK_STAINED_GLASS;
            case '1' -> Material.BLUE_STAINED_GLASS;
            case '2' -> Material.GREEN_STAINED_GLASS;
            case '3' -> Material.CYAN_STAINED_GLASS;
            case '4' -> Material.RED_STAINED_GLASS;
            case '5' -> Material.PURPLE_STAINED_GLASS;
            case '6' -> Material.ORANGE_STAINED_GLASS;
            case '7' -> Material.LIGHT_GRAY_STAINED_GLASS;
            case '8' -> Material.GRAY_STAINED_GLASS;
            case '9' -> Material.LIGHT_BLUE_STAINED_GLASS;
            case 'a' -> Material.LIME_STAINED_GLASS;
            case 'b' -> Material.CYAN_STAINED_GLASS;
            case 'c' -> Material.RED_STAINED_GLASS;
            case 'd' -> Material.PINK_STAINED_GLASS;
            case 'e' -> Material.YELLOW_STAINED_GLASS;
            case 'f' -> Material.WHITE_STAINED_GLASS;
            default -> Material.GLASS;
        };
    }
}
