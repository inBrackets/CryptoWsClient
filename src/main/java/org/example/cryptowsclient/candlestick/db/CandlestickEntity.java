package org.example.cryptowsclient.candlestick.db;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "candlestick")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandlestickEntity {

    @EmbeddedId
    private CandlestickId id;

    @Column(name = "openPrice")
    private BigDecimal openPrice;

    @Column(name = "highPrice")
    private BigDecimal highPrice;

    @Column(name = "lowPrice")
    private BigDecimal lowPrice;

    @Column(name = "closePrice")
    private BigDecimal closePrice;

    @Column(name = "volume")
    private BigDecimal volume;
}
