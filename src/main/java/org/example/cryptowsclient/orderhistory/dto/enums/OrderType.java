package org.example.cryptowsclient.orderhistory.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderType {
    MARKET,
    LIMIT,
    STOP_LOSS,
    STOP_LIMIT,
    TAKE_PROFIT,
    TAKE_PROFIT_LIMIT;

    @JsonCreator
    public static OrderType fromValue(String value) {
        return OrderType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

}