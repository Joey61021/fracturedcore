package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.config.Config;
import com.fractured.team.Team;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener
{
    public static final List<String> BLOCKED_COMMANDS;

    private static final String BLOCKED_COMMANDS_PATH = "blocked_commands";

    static
    {
        Config config = FracturedCore.getFracturedConfig();

        // Directly fetch the list from the path
        BLOCKED_COMMANDS = config.getStringList(BLOCKED_COMMANDS_PATH);

        if (BLOCKED_COMMANDS.isEmpty())
        {
            throw new IllegalArgumentException("Blocked commands list is empty or invalid at path: " + BLOCKED_COMMANDS_PATH);
        }
    }

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent event)
    {
        String command = event.getMessage().split(" ")[0].toLowerCase();
        Player player = event.getPlayer();

        Team team = UserManager.getUser(player).getTeam();
        if (team != null)
        {
            return; // Only restricts players not in a team
        }

        if (BLOCKED_COMMANDS.contains(command))
        {
            event.setCancelled(true);
            player.sendMessage(FracturedCore.getMessages().get(Messages.PROCESS_COMMAND));
        }
    }
}
