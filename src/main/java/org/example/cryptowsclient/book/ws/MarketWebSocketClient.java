package org.example.cryptowsclient.book.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cryptowsclient.book.dto.BookDataMessage;
import org.example.cryptowsclient.book.dto.Heartbeat;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MarketWebSocketClient {

    private static final String WS_URL = "wss://stream.crypto.com/exchange/v1/market";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpMessagingTemplate messagingTemplate;

    private Disposable activeConnection;
    private String lastSubscribeMessage;
    private String lastSubscribedChannel;

    public MarketWebSocketClient(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void connect(String subscribeMessage, String channelName) {
        // close old connection if still alive
        if (activeConnection != null && !activeConnection.isDisposed()) {
            activeConnection.dispose();
            System.out.println("Previous WebSocket connection closed");
        }

        this.lastSubscribeMessage = subscribeMessage;
        this.lastSubscribedChannel = channelName;

        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        activeConnection = client.execute(
                URI.create(WS_URL),
                session -> {
                    // send initial subscription
                    Mono<Void> sendSubscription =
                            session.send(Mono.just(session.textMessage(subscribeMessage)));

                    // receive stream
                    Flux<Void> receive = session.receive()
                            .map(msg -> msg.getPayloadAsText())
                            .flatMap(payload -> handleMessage(session, payload))
                            .doOnError(err -> System.err.println("WebSocket error: " + err.getMessage()))
                            .doOnTerminate(() -> System.out.println("WebSocket connection terminated"))
                            .thenMany(Mono.empty());

                    // scheduled keep-alive every 30s
                    Flux<Void> keepAlive = Flux.interval(Duration.ofSeconds(30))
                            .flatMap(tick -> {
                                String heartbeat = "{\"id\":" + tick + ",\"method\":\"public/respond-heartbeat\"}";
                                return session.send(Mono.just(session.textMessage(heartbeat)));
                            })
                            .thenMany(Mono.empty());

                    return sendSubscription.thenMany(Flux.merge(receive, keepAlive)).then();
                }
        ).subscribe(
                null,
                error -> {
                    System.err.println("Connection failed: " + error.getMessage());
                    scheduleReconnect();
                },
                () -> {
                    System.out.println("Connection closed cleanly");
                    scheduleReconnect();
                }
        );
    }

    private Mono<Void> handleMessage(org.springframework.web.reactive.socket.WebSocketSession session, String payload) {
        try {
            // handle heartbeat
            if (payload.contains("\"method\":\"public/heartbeat\"")) {
                Heartbeat parsed = objectMapper.readValue(payload, Heartbeat.class);
                Heartbeat heartbeatResponse = Heartbeat.builder()
                        .id(parsed.getId())
                        .method("public/respond-heartbeat")
                        .build();
                return session.send(Mono.just(session.textMessage(heartbeatResponse.toJson()))).then();
            }

            // handle book data
            BookDataMessage parsed = objectMapper.readValue(payload, BookDataMessage.class);
            String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("[" + formatted + "] Parsed DTO:\n" + parsed);

            messagingTemplate.convertAndSend("/topic/book", parsed);

        } catch (Exception e) {
            System.err.println("Failed to parse message:\n" + payload);
            e.printStackTrace();
        }
        return Mono.empty();
    }

    private void scheduleReconnect() {
        // wait 5 seconds before reconnecting
        Mono.delay(Duration.ofSeconds(5)).subscribe(t -> {
            System.out.println("Reconnecting to " + lastSubscribedChannel + "...");
            connect(lastSubscribeMessage, lastSubscribedChannel);
        });
    }
}
