CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(320) NOT NULL,
    name VARCHAR(128) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(1024) NOT NULL,
    category_id BIGINT NOT NULL,
    confirmedRequests INT,
    createdOn TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(4096) NOT NULL,
    eventDate TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    paid BOOLEAN NOT NULL,
    participantLimit INT DEFAULT(0),
    publishedOn TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    requestModeration BOOLEAN NOT NULL,
    state VARCHAR(64) NOT NULL,
    title VARCHAR(256) NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_user FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_location FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name varchar(128) NOT NULL ,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(128) NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT fk_compilation_event FOREIGN KEY (event_id) REFERENCES events (id)

);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(32) NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_request_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_request_user FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);
