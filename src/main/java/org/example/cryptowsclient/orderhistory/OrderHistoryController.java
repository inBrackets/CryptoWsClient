package org.example.cryptowsclient.orderhistory;

import org.example.cryptowsclient.common.ApiRequestJson;
import org.example.cryptowsclient.common.ApplicationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.example.cryptowsclient.auth.SigningUtil.signAndParseToJsonString;

@RestController
public class OrderHistoryController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/private/get-order-history")
    public ResponseEntity<String> forwardRequest() {
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

        // Send POST
        ResponseEntity<String> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody());
    }
}
