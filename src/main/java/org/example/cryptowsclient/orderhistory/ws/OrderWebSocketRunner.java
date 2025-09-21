package org.example.cryptowsclient.orderhistory.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cryptowsclient.common.ApiRequestJson;
import org.example.cryptowsclient.common.ApplicationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.example.cryptowsclient.auth.SigningUtil.signAndParseToJsonString;

@Component
public class OrderWebSocketRunner implements CommandLineRunner {

    private final UserWebSocketClient client;

    public OrderWebSocketRunner(UserWebSocketClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) throws JsonProcessingException {
        long nonce = System.currentTimeMillis();
        ApiRequestJson authRequest = ApiRequestJson.builder()
                .id(1L)
                .method("public/auth")
                .apiKey(ApplicationProperties.getApiKey())
                .nonce(nonce)
                .build();

        String authMessage = signAndParseToJsonString(authRequest, ApplicationProperties.getApiSecret());

        ApiRequestJson subscribeRequest = ApiRequestJson.builder()
                .id(2L)
                .method("subscribe")
                .params(Map.of("channels", List.of("user.order")))
                .build();

        // The non-auth ws message should not have any nonce, signature, api key or api secret
        String subscribeMessage = new ObjectMapper().writeValueAsString(subscribeRequest);

        client.connect(List.of(authMessage, subscribeMessage), "/topic/user.order");
    }

}