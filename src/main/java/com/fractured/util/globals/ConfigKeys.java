package com.fractured.util.globals;

import com.fractured.config.keys.ConfigKey;

import static com.fractured.config.DataSupplier.*;

public final class ConfigKeys
{
    private ConfigKeys()
    {
    }

    public static final ConfigKey<Boolean> FRIENDLY_FIRE = booleanKey("friendly_fire");
    public static final ConfigKey<Boolean> STARTER_ITEMS = booleanKey("starter_kit");
}
