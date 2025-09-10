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

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal openPrice;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal highPrice;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal lowPrice;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal closePrice;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal volume;
}
