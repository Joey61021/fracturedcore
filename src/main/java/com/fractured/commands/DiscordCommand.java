package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class DiscordCommand
{
    private DiscordCommand()
    {
    }

    public static boolean discord(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_DISCORD));
        return true;
    }
}
