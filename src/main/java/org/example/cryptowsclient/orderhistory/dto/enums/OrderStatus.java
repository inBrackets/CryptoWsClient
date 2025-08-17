package org.example.cryptowsclient.orderhistory.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW, // WS SPECIFIC (user.order.{instrument_name})
    PENDING, // WS SPECIFIC (user.order.{instrument_name})
    REJECTED,
    ACTIVE, // WS SPECIFIC (user.order.{instrument_name})
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