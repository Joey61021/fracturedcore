package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public final class WorldCommand
{
    private WorldCommand()
    {
    }

    private static final SubCommandRegistry worldSubCommands;

    static
    { //todo config messages here
        worldSubCommands = new SubCommandRegistry(0);

        // /world list
        // /world generate [name]
        // /world teleport [name]

        worldSubCommands.register("list", WorldCommand::worldList, "l");
        worldSubCommands.register("generate", WorldCommand::worldGenerate, "gen", "g");
        worldSubCommands.register("teleport", WorldCommand::worldTeleport, "tp");
    }

    public static boolean world(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (args.length == 0 || !worldSubCommands.dispatch(sender, args))
        {
            // usage
        }
        return true;
    }

    private static void worldTeleport(final CommandSender sender, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CONSOLE_BLOCKED));
            return;
        }

        if (!sender.hasPermission(Permissions.COMMAND_WORLD_TELEPORT))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return;
        }

        if (args.length < 2)
        {
            // usage
            return;
        }

        World world = Bukkit.getWorld(args[1]);

        if (world == null)
        {
            sender.sendMessage("world not found");
            return;
        }

        ((Player) sender).teleport(world.getSpawnLocation());
    }

    private static void worldGenerate(final CommandSender sender, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_WORLD_GENERATE))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return;
        }

        // world generate [name]
        if (args.length < 2)
        {
            // usage
            return;
        }

        // todo confirm
        if (Bukkit.getWorld(args[1]) != null)
        {
            sender.sendMessage("A world by that name already exists!");
            return;
        }

        sender.sendMessage("Generating...");
        Bukkit.createWorld(new WorldCreator(args[1]));
        sender.sendMessage("Done.");
    }

    private static void worldList(final CommandSender sender, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_WORLD_LIST))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return;
        }

        StringBuilder builder = new StringBuilder("Worlds\n");
        Iterator<World> worlds = Bukkit.getWorlds().iterator();

        while (worlds.hasNext())
        {
            builder.append("- ").append(worlds.next().getName()).append("\n");
        }

        builder.append("- ").append(worlds.next().getName());
        sender.sendMessage(builder.toString());
    }
}
