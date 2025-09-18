package org.example.cryptowsclient.orderhistory.db;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.CandlestickService;
import org.example.cryptowsclient.orderhistory.OrderHistoryService;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

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
public class StartupOrderHistoryLoader {

    private final OrderHistoryService orderHistoryService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCandlesticksOnStartup() {
        List<OrderItemDto> orderHistoryFromLast24Hours = orderHistoryService.getOrderHistoryFromLast24Hours();
        orderHistoryService.saveToDB(orderHistoryFromLast24Hours);


    }
}