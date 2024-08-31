CREATE TABLE asset (
    asset_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(255) NOT NULL,
    last_value NUMERIC(19, 2) ,
    value_date TIMESTAMP
);
