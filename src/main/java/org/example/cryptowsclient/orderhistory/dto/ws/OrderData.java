package org.example.cryptowsclient.orderhistory.dto.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderData {
    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("client_oid")
    private String clientOid;

    @JsonProperty("instrument_name")
    private String instrumentName;

    @JsonProperty("fee_instrument_name")
    private String feeInstrumentName;

    @JsonProperty("order_type")
    private String orderType;

    @JsonProperty("time_in_force")
    private String timeInForce;

    private String side;

    @JsonProperty("exec_inst")
    private List<String> execInst;

    private String quantity;

    @JsonProperty("limit_price")
    private String limitPrice;

    @JsonProperty("ref_price")
    private String refPrice;

    @JsonProperty("order_value")
    private String orderValue;

    @JsonProperty("maker_fee_rate")
    private String makerFeeRate;

    @JsonProperty("taker_fee_rate")
    private String takerFeeRate;

    @JsonProperty("create_time")
    private long createTime;

    @JsonProperty("create_time_ns")
    private String createTimeNs;

    @JsonProperty("order_date")
    private String orderDate;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("avg_price")
    private String avgPrice;

    @JsonProperty("cumulative_quantity")
    private String cumulativeQuantity;

    @JsonProperty("cumulative_value")
    private String cumulativeValue;

    @JsonProperty("cumulative_fee")
    private String cumulativeFee;

    private String status;

    @JsonProperty("update_user_id")
    private String updateUserId;

    @JsonProperty("update_time")
    private long updateTime;

    @JsonProperty("transaction_time_ns")
    private long transactionTimeNs;
}