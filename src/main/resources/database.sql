CREATE TABLE users
(
    id  INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(36) NOT NULL
);

-- A list of all the teams. This is where the TeamCache is derived from.
CREATE TABLE team_entries
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    -- Number of members in the team, dynamically updated as players join and leave
    members INT   NOT NULL,
    -- Spawn location
    s_x DOUBLE NOT NULL,
    s_y DOUBLE NOT NULL,
    s_z DOUBLE NOT NULL,
    s_pi     FLOAT NOT NULL,
    s_ya     FLOAT NOT NULL
);

-- A list of all the claims. Each claim has a corresponding team to which it
-- belongs. Although this plugin only requires that teams have one claim, this
-- system allows for changes in the future.
CREATE TABLE claim_entries
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    team_id INT NOT NULL,
    x0      INT NOT NULL,
    z0      INT NOT NULL,
    x1      INT NOT NULL,
    z1      INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES team_entries (id)
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
    FOREIGN KEY (team_id) REFERENCES team (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE team_members
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    team_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES team_entries (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE beacons
(
    id int AUTO_INCREMENT PRIMARY KEY,
    x int NOT NULL,
    y int NOT NULL,
    z int NOT NULL
);