package com.fractured.storage;

import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.util.Config;

public interface Storage
{
    static Storage newStorage(Config config)
    {
        String database_type = config.getString("storage.type");

        return switch (database_type)
        {
            case "mysql" -> new HikariStorage(
                    config.getString("storage.mysql.database"),
                    config.getString("storage.mysql.username"),
                    config.getString("storage.mysql.password"),
                    config.getString("storage.mysql.host"),
                    config.getInt("storage.mysql.port"));
            case null, default -> throw new IllegalArgumentException("Unknown database type: " + database_type);
        };
    }

    void initServerResources();

    void assignTeam(User user, Team team);

    void removeTeam(User user);

    void load(User user);

    void save(User user);
}
