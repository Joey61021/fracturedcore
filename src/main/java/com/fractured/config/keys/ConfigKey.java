package com.fractured.config.keys;

import com.fractured.config.Config;
import com.fractured.config.DataSupplier;

public class ConfigKey<T>
{
    private final String path;
    private final DataSupplier<T> getter;
    private int index = -1;
    private boolean reloadable;

    public ConfigKey(DataSupplier<T> getter, String path)
    {
        this.path       = path;
        this.getter     = getter;
        this.reloadable = true;
    }

    public String getPath()
    {
        return path;
    }

    public T get(Config config)
    {
        return getter.getValue(config, path);
    }

    public int ordinal()
    {
        return this.index;
    }

    public boolean reloadable()
    {
        return this.reloadable;
    }

    public void setOrdinal(int ordinal)
    {
        if (this.index != -1)
        {
            throw new IllegalStateException("Cannot initialize ConfigKey twice!");
        }
        this.index = ordinal;
    }

    public void setReloadable(boolean reloadable)
    {
        this.reloadable = reloadable;
    }
}