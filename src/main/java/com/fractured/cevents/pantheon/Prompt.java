package com.fractured.cevents.pantheon;

import net.kyori.adventure.text.Component;

public final class Prompt
{
    private Component prompt;
    private final Response response;

    public Prompt(Component prompt, Response response)
    {
        this.prompt = prompt;
        this.response = response;
    }

    public Prompt(String prompt, Response response)
    {
        this(Component.text(prompt), response);
    }

    public Component component()
    {
        return prompt;
    }

    public Response response()
    {
        return response;
    }
}
