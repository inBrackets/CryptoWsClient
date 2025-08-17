package org.example.cryptowsclient.orderhistory.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResultDto<GenericData> {

    @JsonProperty("data")
    private List<GenericData> data;

    // specific for WS
    private String subscription;
    private String channel;
    @JsonProperty("instrument_name")
    private String instrumentName;

}
