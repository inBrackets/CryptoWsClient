package org.example.cryptowsclient.orderhistory.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_history")
public class OrderHistoryEntity {

    @Id
    @Column(name = "client_oid")
    private String clientOid;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "time_in_force")
    private String timeInForce;

    @Column(name = "side")
    private String side;

    @Column(name = "exec_inst")
    private String execInst; // stored as JSON string or comma-separated

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "limit_price")
    private BigDecimal limitPrice;

    @Column(name = "order_value")
    private BigDecimal orderValue;

    @Column(name = "maker_fee_rate")
    private BigDecimal makerFeeRate;

    @Column(name = "taker_fee_rate")
    private BigDecimal takerFeeRate;

    @Column(name = "avg_price")
    private BigDecimal avgPrice;

    @Column(name = "cumulative_quantity")
    private BigDecimal cumulativeQuantity;

    @Column(name = "cumulative_value")
    private BigDecimal cumulativeValue;

    @Column(name = "cumulative_fee")
    private BigDecimal cumulativeFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "update_user_id")
    private String updateUserId;

    @Column(name = "order_date")
    private String orderDate; // or LocalDate if you prefer parsing to date

    @Column(name = "instrument_name")
    private String instrumentName;

    @Column(name = "fee_instrument_name")
    private String feeInstrumentName;

    @Column(name = "reason")
    private Integer reason;

    @Column(name = "create_time")
    private Long createTime;  // epoch millis

    @Column(name = "create_time_ns")
    private String createTimeNs;

    @Column(name = "update_time")
    private Long updateTime; // epoch millis
}
