package org.example.cryptowsclient.orderhistory.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("order_id")
    private String orderId; // numeric but returned as string

    @JsonProperty("client_oid")
    private String clientOid;

    @JsonProperty("order_type")
    private OrderType orderType;

    @JsonProperty("time_in_force")
    private TimeInForce timeInForce;

    @JsonProperty("side")
    private Side side;

    @JsonProperty("exec_inst")
    private List<ExecInst> execInst;

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("limit_price")
    private String limitPrice;

    @JsonProperty("order_value")
    private String orderValue;

    @JsonProperty("maker_fee_rate")
    private String makerFeeRate;

    @JsonProperty("taker_fee_rate")
    private String takerFeeRate;

    @JsonProperty("avg_price")
    private String avgPrice;

    @JsonProperty("ref_price")
    private String refPrice;

    @JsonProperty("ref_price_type")
    private String refPriceType;


    @JsonProperty("cumulative_quantity")
    private String cumulativeQuantity;

    @JsonProperty("cumulative_value")
    private String cumulativeValue;

    @JsonProperty("cumulative_fee")
    private String cumulativeFee;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("update_user_id")
    private String updateUserId;

    @JsonProperty("order_date")
    private String orderDate;

    @JsonProperty("create_time")
    private Long createTime; // timestamp (ms)

    @JsonProperty("create_time_ns")
    private String createTimeNs; // timestamp (ns) as string

    @JsonProperty("update_time")
    private Long updateTime; // timestamp (ms)

    @JsonProperty("transaction_time_ns")
    private String transactionTimeNs; // nanosecond timestamp (string)

    @JsonProperty("instrument_name")
    private String instrumentName;

    @JsonProperty("fee_instrument_name")
    private String feeInstrumentName;

    @JsonProperty("reason")
    private String reason;

    // ==== ENUMS ====

    @Getter
    public enum OrderType {
        MARKET, LIMIT, STOP_LOSS, STOP_LIMIT, TAKE_PROFIT, TAKE_PROFIT_LIMIT;

        @JsonCreator
        public static OrderType fromValue(String value) {
            return OrderType.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }

    @Getter
    public enum TimeInForce {
        GOOD_TILL_CANCEL,
        IMMEDIATE_OR_CANCEL,
        FILL_OR_KILL;

        @JsonCreator
        public static TimeInForce fromValue(String value) {
            return TimeInForce.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }

    @Getter
    public enum Side {
        BUY, SELL;

        @JsonCreator
        public static Side fromValue(String value) {
            return Side.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }

    @Getter
    public enum ExecInst {
        POST_ONLY,
        LIQUIDATION;

        @JsonCreator
        public static ExecInst fromValue(String value) {
            return ExecInst.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }

    @Getter
    public enum OrderStatus {
        NEW,
        PENDING,
        REJECTED,
        ACTIVE,
        CANCELED,
        FILLED,
        EXPIRED;

        @JsonCreator
        public static OrderStatus fromValue(String value) {
            return OrderStatus.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }
}
