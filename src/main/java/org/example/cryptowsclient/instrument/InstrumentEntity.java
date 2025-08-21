package org.example.cryptowsclient.instrument;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "instruments")
public class InstrumentEntity {

    @Id
    @Column(name = "symbol")
    private String symbol;

    @Column(name = "inst_type")
    private String instType;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "base_ccy")
    private String baseCurrency;

    @Column(name = "quote_ccy")
    private String quoteCurrency;

    @Column(name = "quote_decimals")
    private int quoteDecimals;

    @Column(name = "quantity_decimals")
    private int quantityDecimals;

    @Column(name = "price_tick_size")
    private String priceTickSize;

    @Column(name = "qty_tick_size")
    private String quantityTickSize;

    @Column(name = "max_leverage")
    private String maxLeverage;

    @Column(name = "tradable")
    private boolean tradable;

    @Column(name = "expiry_timestamp_ms")
    private long expiryTimestampMs;

    @Column(name = "beta_product")
    private boolean betaProduct;

    @Column(name = "underlying_symbol")
    private String underlyingSymbol;

    @Column(name = "contract_size")
    private String contractSize;

    @Column(name = "margin_buy_enabled")
    private boolean marginBuyEnabled;

    @Column(name = "margin_sell_enabled")
    private boolean marginSellEnabled;
}
