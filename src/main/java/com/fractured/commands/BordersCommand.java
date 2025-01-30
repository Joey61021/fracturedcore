package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.events.world.WorldManager;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class BordersCommand
{
    private BordersCommand()
    {
    }

    private static final SubCommandRegistry generateSubCommands = new SubCommandRegistry(0);

    static
    {
        generateSubCommands.register("generate", BordersCommand::generate, "gen", "g");
        generateSubCommands.register("extend", BordersCommand::generate, "ext", "e");
    }

    private record GenerateBordersConfirmation(int size) implements Consumer<CommandSender>
    {
        @Override
        public void accept(CommandSender sender)
        {
            Player player = (Player) sender;

            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_GENERATE_START));

            WorldManager.generateTeamBorders(size, player.getLocation());

            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_GENERATE_END));
        }
    }

    private record ExtendBordersConfirmation(int size) implements Consumer<CommandSender>
    {
        @Override
        public void accept(CommandSender sender)
        {
            Player player = (Player) sender;

            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_EXTEND_START));

            WorldManager.extendTeamBorders(size);

            player.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_EXTEND_END));
        }
    }

    // /borders generate [size]
    // /borders extend [size]
    public static boolean borders(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(Permissions.COMMAND_BORDERS_ADMIN))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.NO_PERMISSION));
            return true;
        }

        // not enough args (for either subcommand), or no subcommand found,
        if (args.length < 2 || !generateSubCommands.dispatch(sender, args))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_USAGE));
        }
        return true;
    }

    private static void generate(final CommandSender sender, final String[] args)
    {
        // the sender has already been checked for permissions
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.CONSOLE_BLOCKED));
            return;
        }

        int size;

        try
        {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e)
        {
            // fixme
            sender.sendMessage("Size must be an integer");
            return;
        }

        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_GENERATE_CONFIRM));
        ConfirmationManager.addConfirmation((Player) sender, new GenerateBordersConfirmation(size));
    }

    private static void extend(final CommandSender sender, final String[] args)
    {
        // the sender has already been checked for permissions
        int size;

        try
        {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e)
        {
            // fixme
            sender.sendMessage("Size must be an integer");
            return;
        }

        sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_BORDERS_EXTEND_CONFIRM));
        ConfirmationManager.addConfirmation(sender, new ExtendBordersConfirmation(size));
    }
}
