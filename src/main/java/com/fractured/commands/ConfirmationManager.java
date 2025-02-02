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
    /**
     * consoleConfirms can be set from multiple threads, so a lock is used to make sure
     * it's not set while being accessed.
     */
    private static final Object consoleConfirmsLock = new Object();
    private static volatile Consumer<CommandSender> consoleConfirms;

    public static boolean addPlayerConfirmation(Player player, Consumer<CommandSender> subcommand)
    {
        return uuidConfirms.put(player.getUniqueId(), subcommand) != null;
    }

    /**
     * Overrides the previous consoleConfirmation
     * @return true if there was a previous value not null for the console callback
     */
    public static boolean setConsoleConfirmation(Consumer<CommandSender> subcommand)
    {
        synchronized (consoleConfirmsLock)
        {
            boolean rax = consoleConfirms != null;

            consoleConfirms = subcommand;

            return rax;
        }
    }

    public static boolean addConfirmation(CommandSender sender, Consumer<CommandSender> subcommand)
    {
        if (sender instanceof Player)
        {
            return addPlayerConfirmation((Player) sender, subcommand);
        }
        return setConsoleConfirmation(subcommand);
    }

    public static boolean confirm(final CommandSender sender, final Command command, final String label, final String[] args)
    {
        if (sender instanceof Player)
        {
            // sender is a player
            Consumer<CommandSender> callback = uuidConfirms.remove(((Player) sender).getUniqueId());

            if (callback != null)
            {
                callback.accept(sender);
                return true;
            }

            sender.sendMessage("You do not have any pending commands.");
            return true;
        }

        synchronized (consoleConfirmsLock)
        {
            // the sender is console
            if (consoleConfirms != null)
            {
                consoleConfirms.accept(sender);
                consoleConfirms = null;
                return true;
            }
        }

        sender.sendMessage("You do not have any pending commands.");
        return true;
    }

    private ConfirmationManager() { }
}
