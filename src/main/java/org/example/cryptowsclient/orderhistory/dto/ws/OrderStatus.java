package org.example.cryptowsclient.orderhistory.dto.ws;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

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