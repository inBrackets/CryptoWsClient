package org.example.cryptowsclient.orderhistory.ws;

import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus.ACTIVE;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderStatus.CANCELED;

@Service
public class OrderMessageListener {

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
}