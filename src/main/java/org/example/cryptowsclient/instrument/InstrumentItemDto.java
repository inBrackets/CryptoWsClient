package org.example.cryptowsclient.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentItemDto {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("inst_type")
    private String instType;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("base_ccy")
    private String baseCurrency;

    @JsonProperty("quote_ccy")
    private String quoteCurrency;

    @JsonProperty("quote_decimals")
    private int quoteDecimals;

    @JsonProperty("quantity_decimals")
    private int quantityDecimals;

    @JsonProperty("price_tick_size")
    private String priceTickSize;

    @JsonProperty("qty_tick_size")
    private String quantityTickSize;

    @JsonProperty("max_leverage")
    private String maxLeverage;

    @JsonProperty("tradable")
    private boolean tradable;

    @JsonProperty("expiry_timestamp_ms")
    private long expiryTimestampMs;

    @JsonProperty("beta_product")
    private boolean betaProduct;

    @JsonProperty("underlying_symbol")
    private String underlyingSymbol;

    @JsonProperty("contract_size")
    private String contract_size;

    @JsonProperty("margin_buy_enabled")
    private boolean marginBuyEnabled;

    @JsonProperty("margin_sell_enabled")
    private boolean marginSellEnabled;
}