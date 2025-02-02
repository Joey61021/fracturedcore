package com.fractured.commands.subcommand;

import org.bukkit.command.Command;
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
     * It's up to the caller of this to verify the argument length
     * @return True if failed, false if not
     */
    public boolean dispatch(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        SubCommand subCommand = subCommands.get(args[argumentLevel]);

        if (subCommand == null)
        {
            return true;
        }

        subCommand.invoke(sender, cmd, label, args);
        return false;
    }
}
