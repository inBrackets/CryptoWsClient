package org.example.cryptowsclient.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.cryptowsclient.book.dto.SubscribeRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ws")
public class WebSocketController {

    private final CryptoWebSocketClient client;

    public WebSocketController(CryptoWebSocketClient client) {
        this.client = client;
    }

    @PostMapping("/connect")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    public String connectToChannel(@RequestParam String channel) throws JsonProcessingException {
        SubscribeRequest request = SubscribeRequest.builder()
                .id(1)
                .method("subscribe")
                .params(SubscribeRequest.Params.builder()
                        .channel(channel) // dynamic channel
                        .build())
                .nonce(null)
                .build();

        client.connect(request.toJson(), channel);
        return "Subscribed to " + channel;
    }
}