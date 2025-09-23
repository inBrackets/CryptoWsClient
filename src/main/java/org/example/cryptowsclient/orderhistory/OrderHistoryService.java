package org.example.cryptowsclient.orderhistory;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.common.ApiCreateOrderResultDto;
import org.example.cryptowsclient.common.ApiRequestJson;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.example.cryptowsclient.common.ApplicationProperties;
import org.example.cryptowsclient.orderhistory.db.OrderHistoryEntity;
import org.example.cryptowsclient.orderhistory.db.OrderHistoryMapper;
import org.example.cryptowsclient.orderhistory.db.OrderHistoryRepository;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.example.cryptowsclient.orderhistory.dto.enums.OrderType;
import org.example.cryptowsclient.orderhistory.dto.enums.Side;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.example.cryptowsclient.auth.SigningUtil.signAndParseToJsonString;
import static org.example.cryptowsclient.orderhistory.dto.enums.OrderType.LIMIT;

@Service
@AllArgsConstructor
public class OrderHistoryService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryMapper orderHistoryMapper;

    public ResponseEntity<ApiResponseDto<ApiResultDto<OrderItemDto>>> getOrderHistoryFromLast24Hours() {
        String targetUrl = "https://api.crypto.com/exchange/v1/private/get-order-history";

        ApiRequestJson request = ApiRequestJson.builder()
                .method("private/get-order-history")
                .params(Map.of(
                        "instrument_name", "CRO_USD"
                ))
                .apiKey(ApplicationProperties.getApiKey())
                .id(1L)
                .build();

        String requestBody = signAndParseToJsonString(request, ApplicationProperties.getApiSecret());

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<OrderItemDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        // Send POST
        ResponseEntity<ApiResponseDto<ApiResultDto<OrderItemDto>>> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                requestEntity,
                typeRef
        );
        return response;
    }

    public ResponseEntity<ApiResponseDto<ApiResultDto<OrderItemDto>>> getAllOpenOrders() {
        String targetUrl = "https://api.crypto.com/exchange/v1/private/get-open-orders";

        ApiRequestJson request = ApiRequestJson.builder()
                .method("private/get-open-orders")
                .params(Map.of(
                        "instrument_name", "CRO_USD"
                ))
                .apiKey(ApplicationProperties.getApiKey())
                .id(1L)
                .build();

        String requestBody = signAndParseToJsonString(request, ApplicationProperties.getApiSecret());

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<OrderItemDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        // Send POST
        ResponseEntity<ApiResponseDto<ApiResultDto<OrderItemDto>>> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                requestEntity,
                typeRef
        );
        return response;
    }

    public ResponseEntity<ApiResponseDto<ApiCreateOrderResultDto>> createNewOrder(String instrumentName, OrderType orderType, Side side, BigDecimal price, Long quantity) {
        String targetUrl = "https://api.crypto.com/exchange/v1/private/create-order";

        ApiRequestJson request = ApiRequestJson.builder()
                .method("private/create-order")
                .params(Map.of(
                        "instrument_name", instrumentName,
                        "type", LIMIT.name(),
                        "side", side.name(),
                        "price", price.toString(),
                        "quantity", quantity.toString()
                ))
                .apiKey(ApplicationProperties.getApiKey())
                .id(Instant.now().toEpochMilli())
                .build();

        String requestBody = signAndParseToJsonString(request, ApplicationProperties.getApiSecret());

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ParameterizedTypeReference<ApiResponseDto<ApiCreateOrderResultDto>> typeRef =
                new ParameterizedTypeReference<>() {
                };

//        ResponseEntity<String> response1 = restTemplate.exchange(
//                targetUrl,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );

        // Send POST
        ResponseEntity<ApiResponseDto<ApiCreateOrderResultDto>> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                requestEntity,
                typeRef
        );
        return response;
    }

    public void saveToDB(List<OrderItemDto> orderItems) {
        for (OrderItemDto orderItem : orderItems) {
            OrderHistoryEntity entity = orderHistoryMapper.toEntity(orderItem);
            orderHistoryRepository.save(entity);
        }
    }
}
