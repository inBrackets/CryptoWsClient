package org.example.cryptowsclient.orderhistory.dto.ws;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

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