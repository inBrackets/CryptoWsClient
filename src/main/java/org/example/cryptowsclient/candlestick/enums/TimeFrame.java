package org.example.cryptowsclient.candlestick.enums;

import lombok.Getter;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum TimeFrame {

    ONE_MINUTE("1m", Duration.ofMinutes(1)),
    FIVE_MINUTES("5m", Duration.ofMinutes(5)),
    FIFTEEN_MINUTES("15m", Duration.ofMinutes(15)),
    HALF_HOUR("30m", Duration.ofMinutes(30)),
    ONE_HOUR("1h", Duration.ofHours(1)),
    TWO_HOURS("2h", Duration.ofHours(2)),
    FOUR_HOURS("4h", Duration.ofHours(4)),
    TWELVE_HOURS("12h", Duration.ofHours(12)),
    ONE_DAY("1D", Duration.ofDays(1)),
    ONE_WEEK("7D", Duration.ofDays(7)),
    TWO_WEEKS("14D", Duration.ofDays(14));
    // ONE_MONTH("1M", Duration.ofDays(30)); not constant

    private final String symbol;
    private final Duration duration;

    TimeFrame(String symbol, Duration duration) {
        this.symbol = symbol;
        this.duration = duration;
    }

    private static final Map<String, TimeFrame> SYMBOL_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(TimeFrame::getSymbol, tf -> tf));

    public static TimeFrame fromSymbol(String symbol) {
        TimeFrame tf = SYMBOL_MAP.get(symbol);
        if (tf == null) {
            throw new IllegalArgumentException("Unknown timeframe symbol: " + symbol);
        }
        return tf;
    }

}
