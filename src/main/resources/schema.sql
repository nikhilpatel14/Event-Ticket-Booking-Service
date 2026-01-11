CREATE TABLE IF NOT EXISTS event (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS seat (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    seat_row VARCHAR(5) NOT NULL,
    seat_col INT NOT NULL,
    status VARCHAR(10) NOT NULL,
    UNIQUE (event_id, seat_row, seat_col)
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL,
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
