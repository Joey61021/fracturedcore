CREATE TABLE users
(
    id  INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(36) NOT NULL UNIQUE
);

CREATE TABLE worlds
(
    id  INT AUTO_INCREMENT PRIMARY KEY,
    -- uuid
    uid VARCHAR(36) NOT NULL UNIQUE
);

-- A list of all the teams. This is where the TeamCache is derived from.
CREATE TABLE teams
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    -- Number of members in the team, dynamically updated as players join and leave
    members INT NOT NULL,
    name    VARCHAR(16),
    color   TINYINT,
    -- Spawn location
    s_world INT,
    s_x DOUBLE,
    s_y DOUBLE,
    s_z DOUBLE,
    s_pi    FLOAT,
    s_ya    FLOAT
);

-- A list of all the claims. Each claim has a corresponding team to which it
-- belongs. Although this plugin only requires that teams have one claim, this
-- system allows for changes in the future.
CREATE TABLE claims
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    team_id  INT NOT NULL,
    world_id INT NOT NULL,
    x0       INT NOT NULL,
    z0       INT NOT NULL,
    x1       INT NOT NULL,
    z1       INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams (id),
    FOREIGN KEY (world_id) REFERENCES worlds (id)
);

CREATE TABLE team_member_changes
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    team_id  INT       NOT NULL,
    user_id  INT       NOT NULL,
    instant  TIMESTAMP NOT NULL,
    added    BOOL      NOT NULL,
    staff_id INT       NOT NULL,
    reason   VARCHAR(255),
    FOREIGN KEY (team_id) REFERENCES teams (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE team_members
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    team_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (team_id) REFERENCES teams (id)
);

CREATE TABLE team_upgrades
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    team_id INT NOT NULL,
    upgrade_id INT NOT NULL,
    upgrade_level INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams (id)
);
