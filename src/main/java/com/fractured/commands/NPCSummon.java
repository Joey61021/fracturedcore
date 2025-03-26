package com.fractured.commands;

import com.fractured.FracturedCore;
import com.fractured.cevents.pantheon.DialogueManager;
import com.fractured.cevents.pantheon.Prompt;
import com.fractured.cevents.pantheon.Response;
import com.fractured.util.globals.Messages;
import com.fractured.util.globals.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class NPCSummon
{
    public static boolean npcsummon(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_CONSOLE_BLOCKED));
            return true;
        }

        if (!sender.hasPermission(Permissions.COMMAND_NPC_SUMMON))
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NO_PERMISSION));
            return true;
        }

        if (args.length < 1)
        {
            sender.sendMessage(FracturedCore.getMessages().get(Messages.COMMAND_NPC_SUMMON_USAGE));
            return true;
        }

        Player player = (Player) sender;
        Response root = new Response("The Gatekeeper. Salutations player, how can I assist you today?", 0);

        Response definePantheon = new Response("The Gatekeeper. The Great Pantheon is a sacred place of the ancients, where the echoes of history reside.", 20);
        root.addPrompt(new Prompt("What is this place?", definePantheon));

        Response bruhWhat = new Response("Erm, what the sigma?", 20);
        root.addPrompt(new Prompt("Do you yearn for the mines?", bruhWhat));

        root.buildPromptSelector();

        DialogueManager.newDialogueHolder(player.getLocation(), "The Gatekeeper", root);

        return true;
    }
}
