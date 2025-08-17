package org.example.cryptowsclient.orderhistory.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResultDto<T> {

    @JsonProperty("data")
    private List<T> data;

    // specific for WS
    private String subscription;
    private String channel;
    @JsonProperty("instrument_name")
    private String instrumentName;

}
