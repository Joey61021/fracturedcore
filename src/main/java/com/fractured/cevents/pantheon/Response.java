package com.fractured.cevents.pantheon;

import com.fractured.FracturedCore;
import net.kyori.adventure.inventory.Book;
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
    private final List<Component> responses;
    /**
     * Inserted into a book with whichever random component was selected.
     */
    private Component promptSelector;
    private final List<Prompt> prompts;
    private final long delay;

    public Response(List<Component> response, long delay)
    {
        this.responses = response;
        this.delay = delay;

        prompts = new ArrayList<>();
    }

    public Response(String response, long delay)
    {
        this(List.of(Component.text(response)), delay);
    }

    public void addPrompt(Prompt prompt)
    {
        prompts.add(prompt);
    }

    public void buildPromptSelector()
    {
        Component page0 = Component.text("\n\n");

        for (int i = 0; i < prompts.size(); ++i)
        {
            Prompt prompt = prompts.get(i);
            page0 = page0.append(Component.text("> ")).append(prompt.component().clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/advance " + i))).append(Component.text("\n"));
        }

        promptSelector = page0;
    }

    /**
     * Gets a random response from within the responses list using {@link Math#random()}
     */
    private Component getRandomResponse()
    {
        return responses.get((int) (Math.random() * responses.size()));
    }

    private void sendReply(Player player)
    {
        Component component = getRandomResponse();

        player.sendMessage(component);

        player.openBook(Book.book(Component.empty(), Component.empty(), component.append(promptSelector)));
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
        } else
        {
            // delay doesn't exist, no need to schedule
            sendReply(player);
        }
    }

    public Prompt getPromptClicked(int option)
    {
        if (option < prompts.size())
        {
            return prompts.get(option);
        }
        return null;
    }
}
