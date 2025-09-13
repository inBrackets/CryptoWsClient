package org.example.cryptowsclient.candlestick.db;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.CandlestickService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.example.cryptowsclient.candlestick.enums.TimeFrame.FIFTEEN_MINUTES;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.FIVE_MINUTES;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.FOUR_HOURS;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.HALF_HOUR;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.ONE_DAY;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.ONE_HOUR;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.ONE_MINUTE;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.ONE_WEEK;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.TWELVE_HOURS;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.TWO_HOURS;
import static org.example.cryptowsclient.candlestick.enums.TimeFrame.TWO_WEEKS;

@Component
@AllArgsConstructor
public class StartupCandlestickLoader {

    private final CandlestickService candlestickService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCandlesticksOnStartup() {
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_MINUTE, 1);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", FIVE_MINUTES, 7);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", FIFTEEN_MINUTES, 7);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", HALF_HOUR, 7);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_HOUR, 7);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", TWO_HOURS, 7);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", FOUR_HOURS, 7);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", TWELVE_HOURS, 14);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_DAY, 30);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_WEEK, 365);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", TWO_WEEKS, 365);


    }
}