package org.example.cryptowsclient.orderhistory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.cryptowsclient.common.ApiRequestJson;
import org.example.cryptowsclient.common.ApplicationProperties;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.example.cryptowsclient.auth.SigningUtil.signAndParseToJsonString;

@RestController
@AllArgsConstructor
public class OrderHistoryController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderHistoryService orderHistoryService;

    @GetMapping("/private/get-order-history")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    public ResponseEntity<List<OrderItemDto>> forwardRequest() {

        ResponseEntity<ApiResponseDto<ApiResultDto<OrderItemDto>>> response = orderHistoryService.getOrderHistoryFromLast24Hours();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody().getResult().getData());
    }
}
