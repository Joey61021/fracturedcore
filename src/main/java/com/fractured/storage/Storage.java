package com.fractured.storage;

import com.fractured.config.Config;
import com.fractured.team.Claim;
import com.fractured.team.Team;
import com.fractured.user.User;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public interface Storage
{
    static Storage newStorage(Config config)
    {
        String type = config.getString("storage.type");

        switch (type)
        {
            case "test":
                return new TestStorage();
            case "mysql":

                String url = config.getString("storage.mysql.url");
                if (url != null)
                {
                    return new HikariStorage(url, config.getString("storage.mysql.username"), config.getString("storage.mysql.password"));
                }

                return new HikariStorage(
                        config.getString("storage.mysql.database"),
                        config.getString("storage.mysql.username"),
                        config.getString("storage.mysql.password"),
                        config.getString("storage.mysql.host"),
                        config.getInt("storage.mysql.port"));
            default:
                throw new IllegalArgumentException("Unknown database type: " + type);
        }
    }

    void saveClaim(World world, Claim claim);

    void initServerResources();

    //void saveDeath(PlayerDeathEvent event);

    void setClaims(Claim yellow, Claim green, Claim red, Claim blue);

    void setTeam(CommandSender staff, String reason, User user, Team team);

    void removeTeam(CommandSender staff, String reason, User user, Team team);

    /**
     * Called from {@link org.bukkit.event.player.AsyncPlayerPreLoginEvent} to load all of this player's corresponding data
     */
    void load(User user);

    /**
     * Called from {@link org.bukkit.event.player.PlayerQuitEvent}
     * @param user
     */
    void save(User user);
}
