package com.fractured.user;

import com.fractured.cevents.pantheon.Response;
import org.bukkit.entity.Player;

public final class PantheonMeta implements EventMeta
{
    /**
     * Last dialogue holder that this user spoke to
     */
    private Integer dialogueHolder;
    /**
     * The message that responseSender sent.
     */
    private Response lastMessage;
    public final GateKeeperDialogueState gateKeeperStateRoot;

    public PantheonMeta()
    {
        gateKeeperStateRoot = new GateKeeperDialogueState();
    }

    public Integer getDialogueHolder()
    {
        return dialogueHolder;
    }

    public void startConversation(int id, Response response)
    {
        dialogueHolder = id;
        lastMessage = response;
    }

    public void promptClicked(int option, Player player)
    {
        if (lastMessage != null)
        {
            lastMessage = lastMessage.getNextResponse(option);
            lastMessage.send(player);
        }
    }
}
