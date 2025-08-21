package org.example.cryptowsclient.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResultDto<GenericData> {

    @JsonProperty("data")
    private List<GenericData> data;

    // specific for WS
    private String subscription;
    private String channel;
    @JsonProperty("instrument_name")
    private String instrumentName; // candlestick

    private String interval; // candlestick

}
