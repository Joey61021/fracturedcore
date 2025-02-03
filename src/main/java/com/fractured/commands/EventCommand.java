package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.cevents.CEvent;
import com.fractured.cevents.EventManager;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class EventCommand
{
    private EventCommand()
    {
    }

    private static final SubCommandRegistry eventSubCommands = new SubCommandRegistry(0);

    static
    {
        eventSubCommands.register("start", EventCommand::start);
        eventSubCommands.register("stop", EventCommand::stop, "terminate", "skip");
    }

    // /event - information on currently run events
    // /event start [event]
    // /borders stop
    public static boolean event(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        FracturedCore.runAsync(() ->
        {
            if (!(sender instanceof Player player))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
                return;
            }

            if (!player.hasPermission(Permissions.COMMAND_BORDERS_ADMIN))
            {
                player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
                return;
            }

            // not enough args (for either subcommand), or no subcommand found,
            if (args.length < 2 || eventSubCommands.dispatch(player, cmd, label, args))
            {
                player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_EVENT_USAGE));
            }
        });
        return true;
    }

    /**
     * @apiNote ran async
     */
    private static void start(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        Player player = (Player) sender;

        if (EventManager.getRunningEvent() != null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_EVENT_ACTIVE));
            return;
        }

        for (CEvent cEvent : EventManager.events)
        {
            if (cEvent.getName().equalsIgnoreCase(args[1]))
            {
                EventManager.runEvent(cEvent);
                return;
            }
        }

        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_EVENT_INVALID));
    }

    private static void stop(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        Player player = (Player) sender;

        if (EventManager.getRunningEvent() == null)
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_EVENT_NONE_ACTIVE));
            return;
        }

        CEvent event = EventManager.getRunningEvent();
        EventManager.terminateEvent();
        player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_EVENT_STOPPED).replace("%event%", event.getColor() + event.getName()));
    }
}
