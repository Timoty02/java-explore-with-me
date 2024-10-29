CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(2000) NOT NULL,
    email VARCHAR(2000) NOT NULL UNIQUE,
    is_admin BOOLEAN NOT NULL
);
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(2000) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    title VARCHAR(2000) NOT NULL,
    pinned BOOLEAN NOT NULL
);
CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    title VARCHAR(2000) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    category_id BIGINT REFERENCES categories (id) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT REFERENCES users (id) NOT NULL,
    created_on TIMESTAMP NOT NULL,
    published_on TIMESTAMP,
    location_lat FLOAT NOT NULL,
    location_lng FLOAT NOT NULL,
    state VARCHAR(2000) NOT NULL,
    views BIGINT NOT NULL,
    paid BOOLEAN NOT NULL,
    confirmed_requests BIGINT NOT NULL,
    participant_limit BIGINT NOT NULL,
    request_moderation BOOLEAN NOT NULL
);
CREATE TABLE IF NOT EXISTS event_compilation (
    event_id BIGINT,
    compilation_id BIGINT,
    PRIMARY KEY (event_id, compilation_id),
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);
CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    event_id BIGINT REFERENCES events (id) NOT NULL,
    requester_id BIGINT REFERENCES users (id) NOT NULL,
    created TIMESTAMP NOT NULL,
    status VARCHAR(2000) NOT NULL
);
CREATE OR REPLACE FUNCTION update_confirmed_requests()
RETURNS TRIGGER AS '
BEGIN
    -- Обновляем количество одобренных заявок для события
    UPDATE events
    SET confirmed_requests = (
        SELECT COUNT(*)
        FROM requests
        WHERE event_id = NEW.event_id AND status = ''approved''
    )
    WHERE id = NEW.event_id;

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER requests_update_trigger
AFTER INSERT OR UPDATE ON requests
FOR EACH ROW
EXECUTE FUNCTION update_confirmed_requests();