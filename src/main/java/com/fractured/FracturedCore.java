package com.fractured;

import com.fractured.commands.*;
import com.fractured.config.Config;
import com.fractured.events.*;
import com.fractured.events.inventory.InventoryClickListener;
import com.fractured.events.inventory.InventoryCloseListener;
import com.fractured.events.world.WorldManager;
import com.fractured.menu.MenuManager;
import com.fractured.storage.Storage;
import com.fractured.user.UserManager;
import com.fractured.util.globals.ConfigKeys;
import com.fractured.util.globals.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FracturedCore extends JavaPlugin {
    /* I've switched this to a more functional design. Instead of relying on the
       FracturedCore plugin instance to gain access to these, or just passing an
       instance of these to where ever it's needed during initialization, and
       then storing those references in the class that it's passed to (dependency
       injection), I've made all of these static, since there only ever needs
       to exist one of these objects in the code at a time. */

    private static Config config;
    private static Config messages;
    private static Storage storage;

    /**
     * This is just used for scheduling, please don't access this anywhere outside of this class.
     */
    private static FracturedCore plugin;

    public FracturedCore()
    {
        plugin = this;
    }

    // You no longer need to pass the plugin to the scheduler
    public static void runAsync(Runnable runnable)
    {
        // todo switch to virtual threads?
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static Config getFracturedConfig()
    {
        return config;
    }

    public static Config getMessages()
    {
        return messages;
    }

    public static Storage getStorage()
    {
        return storage;
    }

    private MenuManager menuManager;

    private void registerEvents() {
        final PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new UserManager(), this);
        manager.registerEvents(menuManager, this);
        manager.registerEvents(new WorldManager(), this);
        manager.registerEvents(new ChatListener(), this);
        manager.registerEvents(new DeathListener(), this);
        manager.registerEvents(new InventoryCloseListener(), this);
        manager.registerEvents(new JoinListener(), this);
        manager.registerEvents(new LeaveListener(), this);
        manager.registerEvents(new InventoryClickListener(), this);

        manager.registerEvents(new KillListener(), this);
        manager.registerEvents(new RespawnListener(), this);
        manager.registerEvents(new SleepListener(), this);
    }

    private void registerCommands()
    {
        getCommand("coords").setExecutor(CoordsCommand::coords);
        getCommand("teamchat").setExecutor(TeamChatCommand::teamchat);
        getCommand("team").setExecutor(TeamCommand::team);
        getCommand("settings").setExecutor(SettingsCommand::settings);
        getCommand("borders").setExecutor(BordersCommand::borders);
        getCommand("discord").setExecutor(DiscordCommand::discord);
        getCommand("upgrades").setExecutor(UpgradesCommand::upgrades);
        getCommand("confirm").setExecutor(ConfirmationManager::confirm);
        getCommand("world").setExecutor(WorldCommand::world);
        getCommand("sethome").setExecutor(SetHomeCommand::sethome);
        getCommand("home").setExecutor(HomeCommand::home);
    }

    @Override
    public void onLoad()
    {
        // load
    }

    @Override
    public void onEnable() {
        config = new Config(this, ConfigKeys.class, "config.yml");
        messages = new Config(this, Messages.class, "messages.yml");
        storage = Storage.newStorage(config);

        menuManager = new MenuManager();

        registerEvents();
        registerCommands();

        // After we've regsitered events and managers (LIke MenuManager)
        storage.initServerResources();
    }

    @Override
    public void onDisable()
    {
        // fixme wait for db to flush? Consult the bukkit async scheduler probably
        // Also make any additional db updates for saving like the team cache maybe? Anything

        for (Player players : Bukkit.getOnlinePlayers())
        {
            players.kickPlayer(Messages.PLUGIN_RESTARTING);
        }
    }
}
