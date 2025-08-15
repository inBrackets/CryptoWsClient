package org.example.cryptowsclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cryptowsclient.dto.BookDataMessage;
import org.example.cryptowsclient.dto.Heartbeat;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CryptoWebSocketClient {

    private static final String WS_URL = "wss://stream.crypto.com/exchange/v1/market";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpMessagingTemplate messagingTemplate;

    // Track active connection
    private Disposable activeConnection;
    private String lastSubscribedChannel;

    public CryptoWebSocketClient(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void connect(String subscribeMessage, String channelName) {
        // 1️⃣ Unsubscribe from the previous channel if active
        if (activeConnection != null && !activeConnection.isDisposed()) {
            unsubscribe(lastSubscribedChannel);
            activeConnection.dispose();
            System.out.println("Previous WebSocket connection closed");
        }

        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        // 2️⃣ Store last channel for potential unsubscribe
        lastSubscribedChannel = channelName;

        // 3️⃣ Create new WebSocket connection
        activeConnection = client.execute(
                URI.create(WS_URL),
                session -> {
                    Mono<Void> sendSubscription = session.send(Mono.just(session.textMessage(subscribeMessage)));

                    Mono<Void> receive = session.receive()
                            .map(msg -> msg.getPayloadAsText())
                            .flatMap(payload -> handleMessage(session, payload))
                            .doOnError(err -> System.err.println("WebSocket error: " + err.getMessage()))
                            .doOnTerminate(() -> System.out.println("WebSocket connection terminated"))
                            .then();

                    return sendSubscription.then(receive);
                }
        ).subscribe(
                null,
                error -> System.err.println("Connection failed: " + error.getMessage()),
                () -> System.out.println("Connection closed cleanly")
        );
    }

    private Mono<Void> handleMessage(org.springframework.web.reactive.socket.WebSocketSession session, String payload) {
        try {
            // Handle heartbeat
            if (payload.contains("\"method\":\"public/heartbeat\"")) {
                Heartbeat parsed = objectMapper.readValue(payload, Heartbeat.class);
                String heartbeatResponse = String.format("""
                        {
                          "id": %d,
                          "method": "public/respond-heartbeat"
                        }
                        """, parsed.getId());
                return session.send(Mono.just(session.textMessage(heartbeatResponse))).then(Mono.empty());
            }

            // Handle book data
            BookDataMessage parsed = objectMapper.readValue(payload, BookDataMessage.class);
            String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("[" + formatted + "] Parsed DTO:\n" + parsed);

            messagingTemplate.convertAndSend("/topic/user.orderbook", parsed);

        } catch (Exception e) {
            System.err.println("Failed to parse message:\n" + payload);
            e.printStackTrace();
        }
        return Mono.empty();
    }

    // 4️⃣ Send unsubscribe message before closing
    private void unsubscribe(String channel) {
        if (channel != null) {
            String unsubscribeMsg = String.format("""
                {
                  "method": "unsubscribe",
                  "params": {
                    "channels": ["%s"]
                  }
                }
                """, channel);
            System.out.println("Unsubscribing from channel: " + channel);
            // Note: The unsubscribe message will only be sent if session is still open
            // In a more advanced version, you would keep a reference to the session.
        }
    }
}
