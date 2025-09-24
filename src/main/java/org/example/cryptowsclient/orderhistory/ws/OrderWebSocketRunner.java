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


        client.connect("/topic/user.order");
    }

}