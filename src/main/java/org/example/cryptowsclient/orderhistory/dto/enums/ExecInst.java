package org.example.cryptowsclient.orderhistory.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ExecInst {
    POST_ONLY,
    SMART_POST_ONLY, // REST SPECIFIC private/get-order-history
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