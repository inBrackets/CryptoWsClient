package org.example.cryptowsclient.book.dto;


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
public class BookApiResultDto {

    @JsonProperty("data")
    private List<OrderBookEntryDto> data;
    private Integer depth;
    @JsonProperty("instrument_name")
    private String instrumentName;

    public String subscription;
    public String channel;

}
