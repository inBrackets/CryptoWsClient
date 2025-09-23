package org.example.cryptowsclient.orderhistory.ws;

import lombok.AllArgsConstructor;
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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus.ACTIVE;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus.CANCELED;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderType.LIMIT;
import static org.example.cryptowsclient.orderhistory.dto.enums.Side.SELL;

@Service
@AllArgsConstructor
public class OrderMessageListener {

    OrderHistoryService orderHistoryService;

    @EventListener
    public void handleOrderMessage(OrderMessageEvent event) {
        ApiResponseDto<ApiResultDto<OrderItemDto>> payload = event.getPayload();
        // do something (e.g., save to DB, trigger alert, play sound, etc.)
        if(payload.getResult().getData().get(0).getStatus().equals(ACTIVE)) {
            System.out.println("The status ACTIVE has been caught!");
        }
        if(payload.getResult().getData().get(0).getStatus().equals(CANCELED)) {
            System.out.println("The status CANCELED has been caught!");
        }
        System.out.println("**************************************************");
        System.out.println("Listener received order message: " + payload);
    }

    @Scheduled(fixedRate = 5, timeUnit = SECONDS)
    public void debug(){
        ResponseEntity<ApiResponseDto<ApiResultDto<OrderItemDto>>> openOrders = orderHistoryService.getAllOpenOrders();
        // orderHistoryService.createNewOrder("CRO_USD", LIMIT, SELL, new BigDecimal("0.265"), 20L);
    }
}