package com.fractured.storage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.fractured.team.ClaimManager;
import com.fractured.team.Team;
import com.fractured.team.TeamCache;
import com.fractured.team.Claim;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Throw;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HikariStorage implements Storage
{
    private final HikariDataSource hikari;
    private static final int CONSOLE_STAFF_ID = 0;

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

    public HikariStorage(String url, String username, String password)
    {
        HikariConfig hikariConfig = new HikariConfig();

        // init main properties to connect
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

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

    private static final String GET_CLAIMS = "SELECT claims.team_id, claims.x0, claims.z0, claims.x1, claims.z1, worlds.uid FROM claims JOIN worlds ON claims.world_id = worlds.id;";

    private static final String GET_TEAM_CACHE = "SELECT teams.id, teams.members, teams.s_x, teams.s_y, teams.s_z, teams.s_ya, teams.s_pi, teams.color, teams.name, worlds.uid FROM teams JOIN worlds ON worlds.id = teams.s_world;";

    private static World getWorld(String uid)
    {
        return Bukkit.getWorld(UUID.fromString(uid));
    }

    @Override
    public void initServerResources()
    {
        open(conn ->
        {
            // Initialize team cache
            {
                Map<Integer, Team> teamCache = new HashMap<>();

                query(conn.prepareStatement(GET_TEAM_CACHE), rs ->
                {
                    while (rs.next())
                    {
                        int teamId = rs.getInt("id");

                        teamCache.put(teamId,
                                new Team(teamId, rs.getInt("members"), rs.getString("name"), (char) rs.getByte("color"),
                                        new Location(
                                                Bukkit.getWorld(UUID.fromString(rs.getString("uid"))),
                                                rs.getDouble("s_x"), rs.getDouble("s_y"), rs.getDouble("s_z"),
                                                rs.getFloat("s_ya"), rs.getFloat("s_pi"))));
                    }
                });

                TeamCache.init(teamCache);
            }

            {
                // INIT TEAM CLAIMS
                query(conn.prepareStatement(GET_CLAIMS), rs ->
                {
                    while (rs.next())
                    {
                        Team team = TeamCache.getTeam(rs.getInt("team_id"));
                        World world = getWorld(rs.getString("uid"));
                        // get world
                        ClaimManager.newClaim(world, team, rs.getInt("x0"), rs.getInt("z0"), rs.getInt("x1"), rs.getInt("z1"));
                    }
                });
            }
        });
    }

    private static final String UPSERT_CLAIM = """
                                               INSERT INTO claim_entries (team_id, world_id, x0, z0, x1, z1)
                                               VALUES (?, (SELECT id FROM world_entries WHERE world_uid = ?), ?, ?, ?, ?);
                                               """;

    private void saveClaim(Connection conn, World world, Claim claim) throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement(UPSERT_CLAIM);

        stmt.setInt(1, claim.getTeam().getId());
        stmt.setString(2, world.getUID().toString());
        stmt.setInt(3, claim.getX0());
        stmt.setInt(4, claim.getZ0());
        stmt.setInt(5, claim.getX1());
        stmt.setInt(6, claim.getZ1());

        update(stmt);
    }

    @Override
    public void saveClaim(World world, Claim claim)
    {
        open(conn -> saveClaim(conn, world, claim));
    }

    @Override
    public void setClaims(Claim yellow, Claim green, Claim red, Claim blue)
    {
        open(conn ->
        {

        });
    }

    private static final String UPSERT_TEAM_MAPPING = "INSERT INTO team_members (user_id, team_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE team_id = VALUES(team_id);";
    /**
     * Adds to the team assignment table
     */
    private static final String INSERT_MEMBER_CHANGE = "INSERT INTO team_member_changes (team_id, user_id, instant, added, staff_id, reason) VALUES (?, ?, NOW(), ?, ?, ?);";

    /**
     * @return The user id of the sender, or if the sender is console, the console staff id
     */
    private int getUserId(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return UserManager.getUser((Player) sender).getId();
        }

        return CONSOLE_STAFF_ID;
    }

    private static final String INCREMENT_TEAM_MEMBERS = "UPDATE teams SET members = members + 1 WHERE id = ?;";
    private static final String DECREMENT_TEAM_MEMBERS = "UPDATE teams SET members = members - 1 WHERE id = ?;";

    private void updateTeam(Connection conn, int teamId, int userId, boolean adding, int staffId, String reason) throws SQLException
    {
        PreparedStatement stmt;

        {
            // Add to new team
            stmt = conn.prepareStatement(INSERT_MEMBER_CHANGE);

            stmt.setInt(1, teamId);
            stmt.setInt(2, userId);
            stmt.setBoolean(3, adding);
            stmt.setInt(4, staffId);
            stmt.setString(5, reason);

            update(stmt);
        }

        {
            stmt = conn.prepareStatement(UPSERT_TEAM_MAPPING);

            stmt.setInt(1, userId);
            if (adding)
            {
                stmt.setInt(2, teamId);
            } else
            {
                stmt.setNull(2, Types.INTEGER);
            }

            update(stmt);
        }

        {
            if (adding)
            {
                // increment team's members
                stmt = conn.prepareStatement(INCREMENT_TEAM_MEMBERS);
            } else
            {
                // decrement team's members
                stmt = conn.prepareStatement(DECREMENT_TEAM_MEMBERS);
            }

            stmt.setInt(1, teamId);

            update(stmt);
        }
    }

    /**
     * Logs the removal of the player from their current team, and updates the team mapping
     */
    @Override
    public void removeTeam(CommandSender staff, String reason, User user, Team team)
    {
        open(conn -> updateTeam(conn, team.getId(), user.getId(), false, getUserId(staff), reason));
    }

    /**
     * Logs the addition of a player to a new team, and updates the team mapping.
     */
    @Override
    public void setTeam(CommandSender staff, String reason, User user, Team team)
    {
        open(conn -> updateTeam(conn, team.getId(), user.getId(), true, getUserId(staff), reason));
    }

    private static final String SELECT_USER_ID = "SELECT id FROM users WHERE uid = ?;";
    // users.uid is set to unique, so if there's a duplicate key it will be ignored
    private static final String INSERT_IGNORE_USER_ID = "INSERT IGNORE INTO users (uid) VALUE (?);";
    private static final String GET_TEAM_FROM_USER_ID = "SELECT team_id FROM team_members WHERE user_id = ?;";

    @Override
    public void load(User user)
    {
        open(conn ->
        {
            PreparedStatement stmt;

            {
                stmt = conn.prepareStatement(INSERT_IGNORE_USER_ID);

                // Query for the user's uuid
                stmt.setString(1, user.getUuid().toString());

                update(stmt);
            }

            {
                stmt = conn.prepareStatement(SELECT_USER_ID);

                stmt.setString(1, user.getUuid().toString());

                query(stmt, rs ->
                {
                    if (!rs.next())
                    {
                        throw new SQLException("Failed to select user_id for: " + user.getUuid());
                    }

                    user.setUserId(rs.getInt("id"));
                });
            }

            {
                stmt = conn.prepareStatement(GET_TEAM_FROM_USER_ID);

                stmt.setInt(1, user.getId());

                query(stmt, rs ->
                {
                    // If the user has a team
                    if (rs.next())
                    {
                        user.setTeam(TeamCache.getTeam(rs.getInt("team_id")));
                    }
                });
            }
        });
    }

    @Override
    public void save(User user)
    {
        // called when the user logs off
    }
}
