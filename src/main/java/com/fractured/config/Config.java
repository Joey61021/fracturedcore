package com.fractured.config;

import com.fractured.config.keys.ConfigKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Config extends YamlConfiguration
{
    private final JavaPlugin plugin;
    private File file;

    private final Object[] cache;
    private final List<ConfigKey<?>> keys;

    public Config(JavaPlugin plugin, Class<?> keysClass, String def)
    {
        this.plugin = plugin;
        init(this.plugin.getDataFolder(), def, def);

        keys = createKeys(keysClass);
        cache = new Object[keys.size()];

        loadValues(true); // called after cache is initialized
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ConfigKey<T> key)
    {
        return (T) cache[key.ordinal()];
    }

    private List<ConfigKey<?>> createKeys(Class<?> keyClass)
    {
        List<ConfigKey<?>> keys = Arrays.stream(keyClass.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> ConfigKey.class.isAssignableFrom(field.getType()))
                .map(field ->
                {
                    try
                    {
                        return (ConfigKey<?>) field.get(null); // get the static field
                    } catch (IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        // set ordinal used to index values in value array
        for (int i = 0; i < keys.size(); ++i)
        {
            keys.get(i).setOrdinal(i);
        }

        return keys;
    }

    private void loadValues(boolean initial)
    {
        for (ConfigKey<?> key : this.keys)
        {
            if (initial || key.reloadable())
            {
                Object obj = key.get(this);
                cache[key.ordinal()] = obj;
            }
        }
    }

    /**
     * Refreshes the ValuesMap with new config values.
     */
    public void refresh()
    {
        this.load();
        this.save();
        this.loadValues(false);
    }

    public void loadDefaults(String def)
    {
        load();
        InputStream is = this.plugin.getResource(def);
        if (is != null)
        {
            try
            {
                this.load(new InputStreamReader(is));
            } catch (IOException | InvalidConfigurationException exception)
            {
                exception.printStackTrace();
            }
        }
        save();
    }

    public boolean init(File path, String name)
    {
        path.mkdirs();
        this.file = new File(path, name);
        if (!this.file.exists())
        {
            try
            {
                this.file.createNewFile();
            } catch (IOException exception)
            {
                exception.printStackTrace();
            }
            return true;
        }
        load();
        save();
        return false;
    }

    public void init(File path, String name, String def)
    {
        if (init(path, name)) loadDefaults(def);
    }

    public void load()
    {
        try
        {
            load(file);
        } catch (IOException | InvalidConfigurationException exception)
        {
            exception.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            save(this.file);
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
