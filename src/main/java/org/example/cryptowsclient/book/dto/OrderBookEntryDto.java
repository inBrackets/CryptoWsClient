package org.example.cryptowsclient.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookEntryDto {

    // Each bid or ask is represented as [price, quantity, number of orders]
    @JsonProperty("bids")
    private List<List<BigDecimal>> bids;

    @JsonProperty("asks")
    private List<List<BigDecimal>> asks;
}