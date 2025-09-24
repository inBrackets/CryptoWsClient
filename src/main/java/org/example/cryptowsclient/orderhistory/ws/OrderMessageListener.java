package org.example.cryptowsclient.orderhistory.ws;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.book.BookRestService;
import org.example.cryptowsclient.book.dto.BookApiResultDto;
import org.example.cryptowsclient.candlestick.CandlestickService;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.example.cryptowsclient.orderhistory.OrderHistoryService;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.example.cryptowsclient.orderhistory.dto.enums.OrderType;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus.ACTIVE;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus.CANCELED;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus.FILLED;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderType.LIMIT;
import static org.example.cryptowsclient.orderhistory.dto.enums.Side.BUY;
import static org.example.cryptowsclient.orderhistory.dto.enums.Side.SELL;

@Service
@AllArgsConstructor
public class OrderMessageListener {

    private final CandlestickService candlestickService;
    OrderHistoryService orderHistoryService;
    BookRestService bookRestService;

    @EventListener
    public void handleOrderMessage(OrderMessageEvent event) {
        ApiResponseDto<ApiResultDto<OrderItemDto>> payload = event.getPayload();
        System.out.println(format("The status %s has been caught!", payload.getResult().getData().get(0).getStatus().name()));

        if(payload.getResult().getData().get(0).getStatus().equals(FILLED) && payload.getResult().getData().get(0).getSide() == SELL) {
            ResponseEntity<ApiResponseDto<BookApiResultDto>> book = bookRestService.getOrderBook("CRO_USD", 10);
            BigDecimal limitPrice = new BigDecimal(event.getPayload().getResult().getData().get(0).getLimitPrice());
            BigDecimal currentPrice = book.getBody().getResult().getData().get(0).getAsks().get(0).get(0);

            // Get the smaller one
            BigDecimal newLimitPrice = limitPrice.min(currentPrice).subtract(new BigDecimal("0.0001"));
            orderHistoryService.createNewOrder("CRO_USD", LIMIT, BUY, newLimitPrice, Long.valueOf(event.getPayload().getResult().getData().get(0).getQuantity()));
        }
        System.out.println("**************************************************");
        System.out.println("Listener received order message: " + payload);
    }

    @Scheduled(fixedRate = 5, timeUnit = SECONDS)
    public void debug(){
        ResponseEntity<ApiResponseDto<BookApiResultDto>> book = bookRestService.getOrderBook("CRO_USD", 10);
        ResponseEntity<ApiResponseDto<ApiResultDto<OrderItemDto>>> openOrders = orderHistoryService.getAllOpenOrders();
        // orderHistoryService.createNewOrder("CRO_USD", LIMIT, SELL, new BigDecimal("0.265"), 20L);
    }
}