package org.example.cryptowsclient.candlestick.db;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandlestickId implements Serializable {
    private String instrumentName;
    private long timestamp;
}
