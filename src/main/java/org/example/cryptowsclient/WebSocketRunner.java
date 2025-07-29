package org.example.cryptowsclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WebSocketRunner implements CommandLineRunner {

    private final CryptoWebSocketReactiveClient client;

    public WebSocketRunner(CryptoWebSocketReactiveClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        client.connect();
    }
}