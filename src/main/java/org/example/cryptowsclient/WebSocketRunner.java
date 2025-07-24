package org.example.cryptowsclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WebSocketRunner implements CommandLineRunner {

    private final CryptoWebSocketClient client;

    public WebSocketRunner(CryptoWebSocketClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        client.connect();
    }
}