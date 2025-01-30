package com.fractured.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class ConfirmationManager
{
    private static final Map<UUID, Consumer<CommandSender>> uuidConfirms = new ConcurrentHashMap<>();
    private static Consumer<CommandSender> consoleConfirms;

    private ConfirmationManager()
    {
    }

    public static boolean addConfirmation(Player player, Consumer<CommandSender> subcommand)
    {
        return uuidConfirms.put(player.getUniqueId(), subcommand) != null;
    }

    public static boolean addConfirmation(CommandSender sender, Consumer<CommandSender> subcommand)
    {
        if (sender instanceof Player)
        {
            return addConfirmation((Player) sender, subcommand);
        }
        return addConsoleConfirmation(subcommand);
    }

    /**
     * Overrides the previous consoleConfirmation
     * @return true if there was a previous value not null for the console callback
     */
    public static boolean addConsoleConfirmation(Consumer<CommandSender> subcommand)
    {
        boolean rax = consoleConfirms != null;

        consoleConfirms = subcommand;

        return rax;
    }

    public static boolean confirm(final CommandSender sender, final Command command, final String label, final String[] args)
    {
        Consumer<CommandSender> callback;

        if (sender instanceof Player)
        {
            callback = uuidConfirms.remove(((Player) sender).getUniqueId());

            if (callback != null)
            {
                callback.accept(sender);
                return true;
            }

            sender.sendMessage("You do not have any pending commands.");
            return true;
        }

        // the sender is console
        if (consoleConfirms != null)
        {
            consoleConfirms.accept(sender);
            consoleConfirms = null;
            return true;
        }

        sender.sendMessage("You do not have any pending commands.");
        return true;
    }
}
