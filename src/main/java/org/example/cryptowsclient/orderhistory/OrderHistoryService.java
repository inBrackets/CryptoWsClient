package org.example.cryptowsclient.orderhistory;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.common.ApiRequestJson;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.example.cryptowsclient.common.ApplicationProperties;
import org.example.cryptowsclient.orderhistory.db.OrderHistoryEntity;
import org.example.cryptowsclient.orderhistory.db.OrderHistoryMapper;
import org.example.cryptowsclient.orderhistory.db.OrderHistoryRepository;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.example.cryptowsclient.auth.SigningUtil.signAndParseToJsonString;

@Service
@AllArgsConstructor
public class OrderHistoryService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryMapper orderHistoryMapper;

    public List<OrderItemDto> getOrderHistoryFromLast24Hours() {
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

        return response.getBody().getResult().getData();
    }

    public void saveToDB(List<OrderItemDto> orderItems) {
        for (OrderItemDto orderItem : orderItems) {
            OrderHistoryEntity entity = orderHistoryMapper.toEntity(orderItem);
            orderHistoryRepository.save(entity);
        }
    }
}
