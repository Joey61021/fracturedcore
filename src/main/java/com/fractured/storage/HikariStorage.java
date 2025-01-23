package com.fractured.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fractured.team.ClaimManager;
import com.fractured.team.Team;
import com.fractured.team.TeamCache;
import com.fractured.team.TeamClaim;
import com.fractured.user.User;
import com.fractured.util.Throw;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class HikariStorage implements Storage
{
    private final HikariDataSource hikari;

    public HikariStorage(String database, String username, String password, String host, int port)
    {
        HikariConfig hikariConfig = new HikariConfig();

        // init main properties to connect
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

//        try
//        {
//            // init other properties
//            for (String key : conf.getConfigurationSection("mysql.datasource_properties")
//                    .getKeys(false))
//            {
//                hikariConfig.addDataSourceProperty(key, conf.getString("mysql.datasource_properties."
//                        + key));
//            }
//        } catch (NullPointerException ignored)
//        {
//            // in the case that the conf section is null (i.e. go with default config)
//        }

        // get data source
        this.hikari = new HikariDataSource(hikariConfig);
    }

    /**
     * @return Returns valid connection from HikariDataSource. Make sure to close this.
     */
    private Connection getConnection() throws SQLException
    {
        if (this.hikari == null)
        {
            throw new SQLException("HikariDataSource is null.");
        }

        Connection conn = this.hikari.getConnection();

        if (conn == null)
        {
            throw new SQLException("Unable to retrieve connection from nonnull HikariDataSource.");
        }
        return conn; // make sure to close
    }

    private void open(Throw.Consumer<Connection, SQLException> foo)
    {
        try (Connection conn = getConnection())
        {
            foo.accept(conn);
        } catch (SQLException sql)
        {
            sql.printStackTrace();
        }
    }

    private static void query(PreparedStatement stmt, Throw.Consumer<ResultSet, SQLException> foo)
    {
        try
        {
            ResultSet rs = stmt.executeQuery();

            foo.accept(rs);

            // Close all resources
            rs.close();
            stmt.close();
        } catch (SQLException sql)
        {
            sql.printStackTrace();
        }
    }

    /**
     * @return -1 if SQLException was thrown
     */
    private static int update(PreparedStatement stmt)
    {
        try
        {
            int updated = stmt.executeUpdate();

            stmt.close();

            return updated;
        } catch (SQLException sql)
        {
            sql.printStackTrace();
        }

        return -1;
    }

    private static int selectLastInsertId(Connection conn) throws SQLException
    {
        // why Java...
        AtomicInteger lastInsertId = new AtomicInteger();

        query(conn.prepareStatement("SELECT LAST_INSERT_ID();"), rs ->
        {
            lastInsertId.set((rs.next()) ? rs.getInt(1) : -1);
        });

        return lastInsertId.get();
    }

    private static final String GET_CLAIMS = "SELECT * FROM claims;";

    private static final String GET_TEAM_CACHE = "SELECT * FROM teams;";

    private static final String SELECT_USER_ID = "SELECT id FROM users WHERE uid = ?;";

    private static final String INSERT_USER_ID = "INSERT INTO users (uid) VALUES (?);";

    private static final String GET_TEAM_FROM_USER_ID = "SELECT teamId FROM team_assignments WHERE userId = ?;";

    /**
     * Adds to the team assignment table
     */
    private static final String ADD_TO_TEAM_ASSIGNMENT = "INSERT INTO team_assignment (id, user_id) VALUES (?, ?);";

    public void initServerResources()
    {
        Map<Integer, Team> rax = new HashMap<>();

        open(conn ->
        {
            // INIT TEAM CACHE
            query(conn.prepareStatement(GET_TEAM_CACHE), rs ->
            {
                Team team;

                while (rs.next())
                {
                    team = new Team(rs.getInt("id"), rs.getInt("members"), rs.getString("name"), rs.getString("color"), Material.valueOf(rs.getString("material")),
                            new Location(Bukkit.getWorld(rs.getString("sWorld")), rs.getDouble("sX"), rs.getDouble("sY"), rs.getDouble("sZ"), rs.getFloat("sY"), rs.getFloat("sP")));
                    rax.put(rs.getInt("id"), team);
                }
            });

            TeamCache.init(rax);

            // INIT TEAM CLAIMS
            query(conn.prepareStatement(GET_CLAIMS), rs ->
            {
                while (rs.next())
                {
                    Team team = TeamCache.getTeam(rs.getInt("team_id"));
                    TeamClaim claim = new TeamClaim(team,
                            rs.getInt("x0"), rs.getInt("z0"),
                            rs.getInt("x1"), rs.getInt("z1"));
                    ClaimManager.addClaim(claim);
                }
            });
        });
    }

    @Override
    public void assignTeam(User user, Team team)
    {
        open(conn -> {
            update(conn.prepareStatement(ADD_TO_TEAM_ASSIGNMENT));
        });
    }

    @Override
    public void removeTeam(User user)
    {
        open(conn -> {
            update(conn.prepareStatement(ADD_TO_TEAM_ASSIGNMENT));
        });
    }

    /**
     * Called from AsyncPlayerPreLoginEvent to load all of this player's corresponding data
     */
    @Override
    public void load(User user)
    {
        open(conn ->
        {
            PreparedStatement stmt = conn.prepareStatement(SELECT_USER_ID);

            // Query for the user's uuid
            stmt.setString(1, user.getUuid().toString());

            // This isn't as fast as can be, this could be further optimized.
            // But it's async, and shouldn't be a problem for the server.
            query(stmt, rs ->
            {
                // The user has a userId in the database
                if (rs.next())
                {
                    user.setUserId(rs.getInt("id"));
                } else
                {
                    PreparedStatement stmt2 = conn.prepareStatement(INSERT_USER_ID);
                    // Insert their uuid
                    stmt2.setString(1, user.getUuid().toString());

                    update(stmt2);

                    user.setUserId(selectLastInsertId(conn));
                }
            });

            query(conn.prepareStatement(GET_TEAM_FROM_USER_ID, new String[]{user.getUuid().toString()}), rs ->
            {
                // If the user has a team
                if (rs.next())
                {
                    user.setUserId(rs.getInt("id"));
                    user.setTeam(TeamCache.getTeam(rs.getInt("team_id")));
                }
            });
        });
    }

    @Override
    public void save(User user)
    {
        // called when the user logs off
    }
}
