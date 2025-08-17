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
public class Result {
    private String subscription;
    private String channel;

    @JsonProperty("instrument_name")
    private String instrumentName;

    private List<OrderData> data;
}