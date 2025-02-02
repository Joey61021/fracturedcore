package com.fractured.commands.subcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface SubCommand
{
    void invoke(final CommandSender sender, final Command cmd, final String label, final String[] args);
}
