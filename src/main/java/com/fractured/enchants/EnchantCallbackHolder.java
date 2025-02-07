package com.fractured.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class EnchantCallbackHolder<E extends Event>
{
    private final Map<String, BiConsumer<E, Integer>> callbacks;

    public EnchantCallbackHolder()
    {
        callbacks = new HashMap<>();
    }

    public void register(Enchant key, BiConsumer<E, Integer> callback)
    {
        callbacks.put(key.key().getKey(), callback);
    }

    public BiConsumer<E, Integer> get(NamespacedKey key)
    {
        return callbacks.get(key.getKey());
    }
}
