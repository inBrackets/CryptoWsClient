package org.example.cryptowsclient.book.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.cryptowsclient.book.dto.SubscribeRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WebSocketRunner implements CommandLineRunner {

    private final MarketWebSocketClient client;

    public WebSocketRunner(MarketWebSocketClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) throws JsonProcessingException {

        SubscribeRequest request = SubscribeRequest.builder()
                .id(1)
                .method("subscribe")
                .params(SubscribeRequest.Params.builder()
                        .channel("book.CRO_USD.10")
                        .build())
                .nonce(null)
                .build();
        String subscribeMessage = """
                        {
                          "id": 1,
                          "method": "subscribe",
                          "params": {
                            "channels": ["book.CRO_USD.10"]
                          },
                        "nonce": null
                        }
                        """;
        // client.connect(request.toJson());
    }
}