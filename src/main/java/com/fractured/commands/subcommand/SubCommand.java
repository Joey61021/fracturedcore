package com.fractured.commands.subcommand;

import org.bukkit.command.CommandSender;

public interface SubCommand
{
    void invoke(CommandSender sender, String[] args);
}
