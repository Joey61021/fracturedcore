package com.fractured.commands.subcommand;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class SubCommandRegistry
{
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final int argumentLevel;

    public SubCommandRegistry(int argumentLevel)
    {
        this.argumentLevel = argumentLevel;
    }

    public void register(String s, SubCommand subCommand, String... aliases)
    {
        subCommands.put(s, subCommand);

        for (String alias : aliases)
        {
            subCommands.put(alias, subCommand);
        }
    }

    /**
     * @return True if found, false if not
     */
    public boolean dispatch(CommandSender sender, String[] args)
    {
        SubCommand subCommand = subCommands.get(args[argumentLevel]);

        if (subCommand == null)
        {
            return false;
        }

        subCommand.invoke(sender, args);
        return true;
    }
}
