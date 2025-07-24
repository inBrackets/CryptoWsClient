package org.example.cryptowsclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cryptowsclient.dto.BookDataMessage;
import org.example.cryptowsclient.dto.Heartbeat;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CryptoWebSocketClient {

    private static final String WS_URL = "wss://stream.crypto.com/exchange/v1/market";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void connect() {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(
                URI.create(WS_URL),
                new WebSocketHandler() {
                    @Override
                    public Mono<Void> handle(WebSocketSession session) {
                        String subscribeMessage = """
                            {
                              "id": 1,
                              "method": "subscribe",
                              "params": {
                                "channels": ["book.CRO_USDT.10"]
                              }
                            }
                            """;

                        // Send subscription message first
                        Mono<Void> sendSubscription = session.send(Mono.just(session.textMessage(subscribeMessage)));

                        // Handle incoming messages (including heartbeat)
                        Mono<Void> receive = session.receive()
                                .map(msg -> msg.getPayloadAsText())
                                .flatMap(payload -> {
                                    try {
                                        // Handle heartbeat
                                        if (payload.contains("\"method\":\"public/heartbeat\"")) {
                                            System.out.println("Payload is " + payload.toString());
                                            System.out.println("Heartbeat received — sending response");
                                            Heartbeat parsed = objectMapper.readValue(payload, Heartbeat.class);
                                            String heartbeatResponse = String.format("""
                                                {
                                                  "id": %d,
                                                  "method": "public/respond-heartbeat"
                                                }
                                                """, parsed.getId());
                                            return session.send(Mono.just(session.textMessage(heartbeatResponse)))
                                                    .then(Mono.empty());
                                        }

                                        // Parse and print message
                                        BookDataMessage parsed = objectMapper.readValue(payload, BookDataMessage.class);
                                        System.out.println("✅ Parsed DTO:\n" + parsed);
                                    } catch (Exception e) {
                                        System.err.println("Failed to parse message:\n" + payload);
                                        e.printStackTrace();
                                    }
                                    return Mono.empty();
                                })
                                .doOnError(err -> System.err.println("WebSocket error: " + err.getMessage()))
                                .doOnTerminate(() -> System.out.println("WebSocket connection terminated")).then();

                        return sendSubscription.then(receive);
                    }
                }
        ).subscribe(
                null,
                error -> System.err.println("Connection failed: " + error.getMessage()),
                () -> System.out.println("Connection closed cleanly")
        );
    }
}
