CREATE TABLE portfolio_asset (
    portfolio_asset_id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL,
    asset_id BIGINT NOT NULL,
    quantity DOUBLE PRECISION NOT NULL, -- Double in Java entspricht DOUBLE PRECISION in SQL.
    FOREIGN KEY (portfolio_id) REFERENCES portfolio(portfolio_id),
    FOREIGN KEY (asset_id) REFERENCES asset(asset_id)
);
