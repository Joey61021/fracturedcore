package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.events.world.WorldManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class BordersCommand
{
    private BordersCommand()
    {
    }

    private static final SubCommandRegistry generateSubCommands = new SubCommandRegistry(0);

    static
    {
        generateSubCommands.register("generate", BordersCommand::generate, "gen", "g");
        generateSubCommands.register("extend", BordersCommand::extend, "expand", "exp", "ext", "e");
    }

    // /borders - view border information
    // /borders generate [size]
    // /borders extend [size]
    public static boolean borders(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        FracturedCore.runAsync(() ->
        {
            if (!sender.hasPermission(Permissions.COMMAND_BORDERS_ADMIN))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
                return;
            }

            if (args.length == 0)
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_INFORMATION_HEADER));
                for (World worlds : Bukkit.getWorlds())
                {
                    sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_INFORMATION_COLUMN).replace("%world%", worlds.getName()).replace("%size%", String.valueOf(worlds.getWorldBorder().getSize())));
                }
                return;
            }

            // not enough args (for either subcommand), or no subcommand found,
            if (args.length < 2 || generateSubCommands.dispatch(sender, cmd, label, args))
            {
                sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_USAGE));
            }
        });
        return true;
    }

    /**
     * @apiNote ran async
     */
    private static void generate(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // the sender has already been checked for permissions
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return;
        }

        int size;

        try
        {
            size = Integer.parseInt(args[1]);
        } catch (NumberFormatException e)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.MUST_BE_INT));
            return;
        }

        ConfirmationManager.addPlayerConfirmation((Player) sender, player ->
        {
            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_GENERATE_START));

            WorldManager.generateTeamBorders(size, ((Player) player).getLocation());

            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_GENERATE_END));
        });
        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_GENERATE_CONFIRM));
    }

    private static void extend(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        // the sender has already been checked for permissions
        int size;

        try
        {
            size = Integer.parseInt(args[1]);
        } catch (NumberFormatException e)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.MUST_BE_INT));
            return;
        }

        ConfirmationManager.addConfirmation(sender, verifier ->
        {
            verifier.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_EXTEND_START));

            WorldManager.extendTeamBorders(size);

            verifier.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_EXTEND_END));
        });
        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_EXTEND_CONFIRM));
    }
}
