package org.example.cryptowsclient.candlestick.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandlestickId implements Serializable {

    @Column(name = "instrument_name")
    private String instrumentName;

    @Column(name = "timeframe")
    private String timeframe;

    @Column(name = "timestamp")
    private long timestamp;
}
