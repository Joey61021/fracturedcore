package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.user.EventMeta;
import com.fractured.user.PantheonMeta;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AdvanceCommand
{
    // advancing dialogue
    public static boolean advance(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (args.length < 1)
        {
            // this command doesn't need a usage, as it's only used for advancing dialogue
            return true;
        }

        User user = UserManager.getUser(((Player) sender).getUniqueId());
        EventMeta meta = user.getEventMeta();

        if (meta instanceof PantheonMeta)
        {
            try
            {
                int option = Integer.parseInt(args[0]);

                ((PantheonMeta) meta).promptClicked(option, (Player) sender);
            } catch (NumberFormatException nfe)
            {
            }
        }



        return true;
    }
}
