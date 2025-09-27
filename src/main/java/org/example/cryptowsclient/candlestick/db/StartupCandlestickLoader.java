package org.example.cryptowsclient.candlestick.db;

import javazoom.jl.player.Player;
import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.CandlestickService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
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
    private final int DAYS_COUNT = 1;
    private final AtomicBoolean isDataDownloaded = new AtomicBoolean(false);

    @EventListener(ApplicationReadyEvent.class)
    public void loadCandlesticksOnStartup() {
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_MINUTE, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", FIVE_MINUTES, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", FIFTEEN_MINUTES, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", HALF_HOUR, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_HOUR, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", TWO_HOURS, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", FOUR_HOURS, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", TWELVE_HOURS, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_DAY, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", ONE_WEEK, DAYS_COUNT);
        candlestickService.saveLastDaysCandleSticks("CRO_USD", TWO_WEEKS, DAYS_COUNT);

        isDataDownloaded.set(true);
    }

    @Scheduled(fixedRate = 1, timeUnit = MINUTES)
    public void loadOneMinuteCandlesticks() {
        // the latest candlestick has incomplete volume
        // add&update candlesticks
        candlestickService.saveLastXCandleSticks("CRO_USD", ONE_MINUTE, 3);

        // remove surplus candlesticks
//        long rowsTo_delete = candlestickService.getCandlesticksCountByTimeframe(ONE_MINUTE) - MAX_CANDLESTICKS;
//        for (int i = 0; i < rowsTo_delete; i++) {
//            candlestickService.removeOldestCandlestickByTimeFrame(ONE_MINUTE);
//        }
    }

    @Scheduled(fixedRate = 30, timeUnit = SECONDS)
    public void executeAlgoCondition() {
        if (isDataDownloaded.get()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatted = now.format(formatter);

            try {
                sleep(20L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            candlestickService.saveLastXCandleSticks("CRO_USD", ONE_MINUTE, 1);
            try {
                sleep(20L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            candlestickService.saveLastXCandleSticks("CRO_USD", FIVE_MINUTES, 1);
            try {
                sleep(20L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            candlestickService.saveLastXCandleSticks("CRO_USD", FIFTEEN_MINUTES, 1);

            double rsi1m = candlestickService.calculateLastRsiValue(14, ONE_MINUTE, "CRO_USD");
            double rsi5m = candlestickService.calculateLastRsiValue(14, FIVE_MINUTES, "CRO_USD");
            double rsi15m = candlestickService.calculateLastRsiValue(14, FIFTEEN_MINUTES, "CRO_USD");
            boolean con_1m = rsi1m > 50;
            boolean con_5m = rsi5m > 50;
            boolean con_15m = rsi15m > 50;
            System.out.println(formatted + "Current RSI: 1m=" + rsi1m + ", 5m=" + rsi5m + ", 15m=" + rsi15m);
            if (con_1m && con_5m && con_15m) {
                try (InputStream is = getClass().getResourceAsStream("/sounds/cash-register-purchase.mp3")) {
                    if (is == null) {
                        throw new IllegalStateException("Could not find sound resource in classpath");
                    }
                    Player player = new Player(is);
                    player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(formatted + " :: Time for CRO_USD Execution!!!");
            }
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

    @Scheduled(fixedRate = 30, timeUnit = MINUTES)
    public void loadThirtyMinuteCandlesticks() {
        candlestickService.saveLastXCandleSticks("CRO_USD", HALF_HOUR, 3);
    }

    @Scheduled(fixedRate = 1, timeUnit = HOURS)
    public void loadOneHourCandlesticks() {
        candlestickService.saveLastXCandleSticks("CRO_USD", ONE_HOUR, 3);
    }

    @Scheduled(fixedRate = 2, timeUnit = HOURS)
    public void loadTwoHoursCandlesticks() {
        candlestickService.saveLastXCandleSticks("CRO_USD", TWO_HOURS, 3);
    }

    @Scheduled(fixedRate = 4, timeUnit = HOURS)
    public void loadFourHoursCandlesticks() {
        candlestickService.saveLastXCandleSticks("CRO_USD", TWO_HOURS, 3);
    }


}