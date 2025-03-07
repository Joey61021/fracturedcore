package com.fractured.cevents.pantheon;

import com.fractured.FracturedCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A node
 */
public class Response
{
    private final List<String> responses;
    private final List<Prompt> prompts;
    private final long delay;



    public Response(List<String> response, long delay)
    {
        this.responses = response;
        this.delay = delay;

        prompts = new ArrayList<>();
    }

    public Response(String response, long delay)
    {
        this(List.of(response), delay);
    }

    public void addPrompt(Prompt prompt)
    {
        prompt.setClickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/advance " + prompts.size()));
        prompts.add(prompt);
    }

    /**
     * Gets a random response from within the responses list using {@link Math#random()}
     */
    private String getRandomResponse()
    {
        return responses.get((int) (Math.random() * responses.size()));
    }

    private void sendReply(Player player)
    {
        player.sendMessage(getRandomResponse());

        for (int i = 0; i < prompts.size(); ++i)
        {
            player.sendMessage(prompts.get(i).component());
        }
    }

    private final class ReplyRunnable implements Runnable
    {
        private final Player player;

        public ReplyRunnable(Player player)
        {
            this.player = player;
        }

        @Override
        public void run()
        {
            sendReply(player);
        }
    }

    /**
     * Send the response to the player
     */
    public void send(Player player)
    {
        if (delay != 0)
        {
            FracturedCore.runDelayAsync(new ReplyRunnable(player), delay);
        }
        // delay doesn't exist, no need to schedule
        sendReply(player);
    }

    public Response getNextResponse(int option)
    {
        return prompts.get(option).response();
    }
}
