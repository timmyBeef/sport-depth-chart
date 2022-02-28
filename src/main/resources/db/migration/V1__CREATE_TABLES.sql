CREATE TABLE IF NOT EXISTS sport (
     id BIGSERIAL PRIMARY KEY,
     --name TEXT UNIQUE NOT NULL, => H2 DB can't create index on TEXT...
     name TEXT NOT NULL,
     created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS team (
    id BIGSERIAL PRIMARY KEY,
    sport_id BIGINT NOT NULL REFERENCES sport(id),
    name TEXT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS player (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL REFERENCES team(id),
    player_no INT NOT NULL,
    player_name TEXT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS position (
    id BIGSERIAL PRIMARY KEY,
    sport_id BIGINT NOT NULL REFERENCES sport(id),
    position_name TEXT NOT NULL,
    real_position_name TEXT NOT NULL,
    position_catergory TEXT NOT NULL ,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);


CREATE TABLE IF NOT EXISTS player_position (
    player_id BIGINT REFERENCES player(id),
    position_id BIGINT REFERENCES position(id),
    position_depth INT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (player_id, position_id)
);

-- H2 DB can't create index on TEXT...
--CREATE INDEX index_player_name ON player (player_name);
--CREATE INDEX index_position_name ON position (position_name);