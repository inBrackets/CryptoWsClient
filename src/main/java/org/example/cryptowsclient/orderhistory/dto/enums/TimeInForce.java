package org.example.cryptowsclient.orderhistory.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

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