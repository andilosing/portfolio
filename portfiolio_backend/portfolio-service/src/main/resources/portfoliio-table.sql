CREATE TABLE portfolio (
    portfolio_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL,
    access_code VARCHAR(6)
);
