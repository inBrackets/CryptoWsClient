package org.example.cryptowsclient.orderhistory.ws;

import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;

public class OrderMessageEvent {
    private final ApiResponseDto<ApiResultDto<OrderItemDto>> payload;

    public OrderMessageEvent(ApiResponseDto<ApiResultDto<OrderItemDto>> payload) {
        this.payload = payload;
    }

    public ApiResponseDto<ApiResultDto<OrderItemDto>> getPayload() {
        return payload;
    }
}