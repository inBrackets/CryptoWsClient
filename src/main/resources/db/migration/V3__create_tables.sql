CREATE TABLE instruments
(
    symbol              varchar(50)     NOT NULL,
    inst_type           varchar(30)     NOT NULL,
    display_name        varchar(100)    NOT NULL,
    base_ccy            varchar(20)     NOT NULL,
    quote_ccy           varchar(20)     NOT NULL,
    quote_decimals      int             NOT NULL,
    quantity_decimals   int             NOT NULL,
    price_tick_size     decimal(20, 10) NOT NULL,
    qty_tick_size       decimal(20, 10) NOT NULL,
    max_leverage        int             NOT NULL,
    tradable            boolean         NOT NULL,
    expiry_timestamp_ms bigint          NOT NULL,
    beta_product        boolean         NOT NULL,
    underlying_symbol   varchar(50)     DEFAULT NULL,
    contract_size       decimal(20, 10) DEFAULT NULL,
    margin_buy_enabled  boolean         NOT NULL,
    margin_sell_enabled boolean         NOT NULL,
    PRIMARY KEY (symbol)
);

CREATE TABLE candlestick
(
    instrument_name VARCHAR(16)   NOT NULL,
    timestamp       BIGINT         NOT NULL,
    time_interval   VARCHAR(8)   NOT NULL,
    open_price      NUMERIC(19, 8) NOT NULL,
    high_price      NUMERIC(19, 8) NOT NULL,
    low_price       NUMERIC(19, 8) NOT NULL,
    close_price     NUMERIC(19, 8) NOT NULL,
    volume          NUMERIC(19, 8) NOT NULL,
    PRIMARY KEY (instrument_name, timestamp)
);
