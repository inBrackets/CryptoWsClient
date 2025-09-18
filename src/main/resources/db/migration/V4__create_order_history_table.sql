CREATE TABLE order_history
(
    client_oid          VARCHAR(100) PRIMARY KEY,
    account_id          VARCHAR(100) NOT NULL,
    order_id            VARCHAR(100) NOT NULL,
    order_type          VARCHAR(50),
    time_in_force       VARCHAR(50),
    side                VARCHAR(10),
    exec_inst           TEXT,
    quantity            DECIMAL(36, 18),
    limit_price         DECIMAL(36, 18),
    order_value         DECIMAL(36, 18),
    maker_fee_rate      DECIMAL(36, 18),
    taker_fee_rate      DECIMAL(36, 18),
    avg_price           DECIMAL(36, 18),
    cumulative_quantity DECIMAL(36, 18),
    cumulative_value    DECIMAL(36, 18),
    cumulative_fee      DECIMAL(36, 18),
    status              VARCHAR(50),
    update_user_id      VARCHAR(100),
    order_date          VARCHAR(20),
    instrument_name     VARCHAR(100),
    fee_instrument_name VARCHAR(50),
    reason              INT,
    create_time         BIGINT,
    create_time_ns      VARCHAR(30),
    update_time         BIGINT
);

CREATE INDEX idx_order_history_order_id ON order_history (order_id);
CREATE INDEX idx_order_history_account_id ON order_history (account_id);
