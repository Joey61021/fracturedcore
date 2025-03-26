package com.fractured.user;

import com.fractured.cevents.pantheon.Prompt;
import com.fractured.cevents.pantheon.Response;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PantheonMeta implements EventMeta
{
    /**
     * The message that responseSender sent.
     */
    private Response lastMessage;
    private Component conversation;
    public final GateKeeperDialogueState gateKeeperStateRoot;

    public PantheonMeta()
    {
        gateKeeperStateRoot = new GateKeeperDialogueState();
    }

    public void setLastMessage(Response response)
    {
        lastMessage = response;
    }

    public void promptClicked(int option, Player player)
    {
        if (lastMessage != null)
        {
            Prompt prompt = lastMessage.getPromptClicked(option);

            if (prompt != null)
            {
                player.sendMessage(Component.text(player.getDisplayName() + ChatColor.WHITE + ". ").append(prompt.component()));

                // next response according to that prompt
                lastMessage = prompt.response();
                lastMessage.send(player);
            }
        }
    }
}
