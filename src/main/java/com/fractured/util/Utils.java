package com.fractured.util;

import com.fractured.FracturedCore;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class Utils
{
    private Utils()
    {

    }

    public static String romanNumeral(int num)
    {
        return switch (num)
        {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> Integer.toString(num);
        };
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

    public static void sendPlayerMessage(Player sender, Player receiver, String message)
    {
        User senderUser = UserManager.getUser(sender);
        User receiverUser = UserManager.getUser(receiver);
        if (senderUser == null || receiverUser == null)
        {
            return;
        }

        senderUser.setLastMessage(receiver.getUniqueId());
        receiverUser.setLastMessage(sender.getUniqueId());

        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_MESSAGE_FORMAT_SENDER).replace("%receiver%", receiver.getName()).replace("%message%", message));
        receiver.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_MESSAGE_FORMAT_RECEIVER).replace("%sender%", sender.getName()).replace("%message%", message));

        for (Player players : Bukkit.getOnlinePlayers())
        {
            User user = UserManager.getUser(players);
            if (user == null)
            {
                continue;
            }

            if (user.getSocialSpy())
            {
                players.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_SOCIAL_SPY_FORMAT).replace("%sender%", sender.getName()).replace("%receiver%", receiver.getName()).replace("%message%", message));
            }
        }
    }
}
