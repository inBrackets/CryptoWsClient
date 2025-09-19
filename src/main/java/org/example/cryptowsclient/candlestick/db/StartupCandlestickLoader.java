package org.example.cryptowsclient.candlestick.db;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.CandlestickService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.MINUTES;
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
    private final long MAX_CANDLESTICKS = 100;
    private final int INITIAL_CANDLESTICK_COUNT = 50;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCandlesticksOnStartup() {
        candlestickService.saveLastXCandleSticks("CRO_USD", ONE_MINUTE, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", FIVE_MINUTES, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", FIFTEEN_MINUTES, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", HALF_HOUR, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", ONE_HOUR, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", TWO_HOURS, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", FOUR_HOURS, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", TWELVE_HOURS, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", ONE_DAY, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", ONE_WEEK, INITIAL_CANDLESTICK_COUNT);
        candlestickService.saveLastXCandleSticks("CRO_USD", TWO_WEEKS, INITIAL_CANDLESTICK_COUNT);
    }

    @Scheduled(fixedRate = 1, timeUnit = MINUTES)
    public void loadOneMinuteCandlesticks() {
        // the latest candlestick has incomplete volume
        // add&update candlesticks
        candlestickService.saveLastXCandleSticks("CRO_USD", ONE_MINUTE, 3);

        // remove surplus candlesticks
        long rowsTo_delete = candlestickService.getCandlesticksCountByTimeframe(ONE_MINUTE) - MAX_CANDLESTICKS;
        for (int i = 0; i < rowsTo_delete; i++) {
            candlestickService.removeOldestCandlestickByTimeFrame(ONE_MINUTE);
        }
    }

    @Scheduled(fixedRate = 5, timeUnit = MINUTES)
    public void loadFiveMinuteCandlesticks() {
        candlestickService.saveLastXCandleSticks("CRO_USD", FIVE_MINUTES, 3);
    }

    @Scheduled(fixedRate = 15, timeUnit = MINUTES)
    public void loadFifteenMinuteCandlesticks() {
        candlestickService.saveLastXCandleSticks("CRO_USD", FIFTEEN_MINUTES, 3);
    }
}