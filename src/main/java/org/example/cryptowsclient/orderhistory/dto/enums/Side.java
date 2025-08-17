package org.example.cryptowsclient.orderhistory.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Side {
    BUY,
    SELL;

    @JsonCreator
    public static Side fromValue(String value) {
        return Side.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}