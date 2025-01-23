package com.fractured.util;

import net.md_5.bungee.api.ChatColor;

public class Utils {
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
