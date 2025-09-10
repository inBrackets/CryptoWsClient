package org.example.cryptowsclient.candlestick.db;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.CandlestickService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartupCandlestickLoader {

    private final CandlestickService candlestickService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCandlesticksOnStartup() {
        candlestickService.saveTodayCandleSticks();
    }
}