package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    {
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
        if (args.length == 0 || worldSubCommands.dispatch(sender, cmd, label, args))
        {
            if (sender.hasPermission(Permissions.COMMAND_WORLD_ADMIN))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_WORLD_USAGE));
            } else
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            }
        }
        return true;
    }

    private static void worldTeleport(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return;
        }

        if (!sender.hasPermission(Permissions.COMMAND_WORLD_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_WORLD_TELEPORT_USAGE));
            return;
        }

        World world = Bukkit.getWorld(args[1]);

        if (world == null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.INVALID_WORLD));
            return;
        }

        ((Player) sender).teleport(world.getSpawnLocation());
    }

    private static void worldGenerate(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_WORLD_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        // world generate [name]
        if (args.length < 2)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_WORLD_GENERATE_USAGE));
            return;
        }

        if (Bukkit.getWorld(args[1]) != null)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_WORLD_GENERATE_WORLD_ALREADY_EXISTS));
            return;
        }

        ConfirmationManager.addConfirmation(sender, verifier ->
        {
            verifier.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_WORLD_GENERATE_BEGIN));
            Bukkit.createWorld(new WorldCreator(args[1]));
            verifier.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_WORLD_GENERATE_END));
        });
        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_WORLD_GENERATE_CONFIRM));
    }

    private static void worldList(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_WORLD_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return;
        }

        StringBuilder builder = new StringBuilder("Worlds\n");
        Iterator<World> worlds = Bukkit.getWorlds().iterator();

        while (worlds.hasNext())
        {
            builder.append("- ").append(worlds.next().getName());

            if (worlds.hasNext())
            {
                builder.append("\n");
            }
        }

        sender.sendMessage(builder.toString());
    }
}
