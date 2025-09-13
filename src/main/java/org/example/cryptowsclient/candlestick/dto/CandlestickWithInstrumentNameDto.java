package org.example.cryptowsclient.candlestick.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandlestickWithInstrumentNameDto {

    @JsonProperty("instrumentName")
    private String instrumentName;

    @JsonProperty("timeframe")
    private String timeframe;

    @JsonProperty("o")
    private BigDecimal openPrice;

    @JsonProperty("h")
    private BigDecimal highPrice;

    @JsonProperty("l")
    private BigDecimal lowPrice;

    @JsonProperty("c")
    private BigDecimal closePrice;

    @JsonProperty("v")
    private BigDecimal volume;

    @JsonProperty("t")
    private long timestamp;
}
