CREATE SCHEMA IF NOT EXISTS app;

CREATE TABLE IF NOT EXISTS app.cafe (
    cafe_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    location geography(Point, 4326)
    );