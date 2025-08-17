package org.example.cryptowsclient.orderhistory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cryptowsclient.book.MarketWebSocketClient;
import org.example.cryptowsclient.common.ApiRequestJson;
import org.example.cryptowsclient.common.ApplicationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.example.cryptowsclient.auth.SigningUtil.signAndParseToJsonString;

@Component
public class OrderWebSocketRunner implements CommandLineRunner {

    private final UserWebSocketClient client;
    private final RestTemplate restTemplate = new RestTemplate();

    public OrderWebSocketRunner(UserWebSocketClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) throws JsonProcessingException {
        ApiRequestJson authRequest = ApiRequestJson.builder()
                .id(1L)
                .method("public/auth")
                .apiKey(ApplicationProperties.getApiKey())
                .build();

        String authMessage = signAndParseToJsonString(authRequest, ApplicationProperties.getApiSecret());

        ApiRequestJson subscribeRequest = ApiRequestJson.builder()
                .id(2L)
                .method("subscribe")
                .params(Map.of("channels", List.of("user.order")))
                .build();

        String subscribeMessage = new ObjectMapper().writeValueAsString(subscribeRequest);

        client.connect(List.of(authMessage, subscribeMessage), "user.order");
    }

}