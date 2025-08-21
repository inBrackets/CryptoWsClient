package org.example.cryptowsclient.instrument;

import org.example.cryptowsclient.common.ApiResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InstrumentController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/public/get-instruments")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    public ResponseEntity forwardRequest() {
        String targetUrl = "https://api.crypto.com/exchange/v1/public/get-instruments";


        // Create headers
        HttpHeaders headers = new HttpHeaders();

        // Build the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Send GET
        ResponseEntity response = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                requestEntity,
                ApiResponseDto.class
        );


        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody());
    }
}
