package com.fractured;

import com.fractured.commands.*;
import com.fractured.events.*;
import com.fractured.events.inventory.InventoryClickListener;
import com.fractured.events.inventory.InventoryCloseListener;
import com.fractured.events.world.WorldManager;
import com.fractured.storage.Storage;
import com.fractured.team.ClaimManager;
import com.fractured.team.TeamCache;
import com.fractured.user.UserManager;
import com.fractured.util.Config;
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
    private static Storage storage;
    /**
     * Each {@link Player} has associated a {@link com.fractured.user.User} object.
     * This is to store additional data about the Player. This User object is
     * guaranteed to exist if the Player is online.
     */
    private static UserManager userManager;

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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static Config getFracturedConfig()
    {
        return config;
    }

    public static Storage getStorage()
    {
        return storage;
    }

    public static UserManager getUserManager()
    {
        return userManager;
    }

    private void registerEvents() {
        final PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(userManager = new UserManager(), this);
        manager.registerEvents(new WorldManager(), this);
        manager.registerEvents(new ChatListener(), this);
        manager.registerEvents(new DeathListener(), this);
        manager.registerEvents(new HungerListener(), this);
        manager.registerEvents(new InventoryClickListener(), this);
        manager.registerEvents(new InventoryCloseListener(), this);
        manager.registerEvents(new JoinListener(), this);
        manager.registerEvents(new LeaveListener(), this);
    }

    private void registerCommands() {
        getCommand("coords").setExecutor(new CoordsCmd());
        getCommand("gm").setExecutor(new GmCmd());
        getCommand("me").setExecutor(new MeCmd());
        getCommand("teamchat").setExecutor(new TeamChatCmd());
        getCommand("team").setExecutor(new TeamCmd());
    }

    @Override
    public void onLoad()
    {

    }

    @Override
    public void onEnable() {
        // fixme why pass the plugin and the datafolder? You can derive the datafolder from the plugin
        config = new Config(this, getDataFolder(), "config.yml", "config.yml");
        storage = Storage.newStorage(config);
        storage.initServerResources();

        registerEvents();
        registerCommands();
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
