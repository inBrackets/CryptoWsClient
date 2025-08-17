package org.example.cryptowsclient.orderhistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cryptowsclient.orderhistory.dto.UserOrderResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class UserWebSocketClient {

    private static final String WS_URL = "wss://stream.crypto.com/exchange/v1/user";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpMessagingTemplate messagingTemplate;

    public UserWebSocketClient(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void connect(List<String> initialMessages, String topic) {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(
                URI.create(WS_URL),
                session -> {
                    // Send all initial messages (auth, then subscribe)
                    Mono<Void> sendMessages = session.send(
                            Flux.fromIterable(initialMessages)
                                    .map(session::textMessage)
                    );

                    Mono<Void> receive = session.receive()
                            .map(msg -> msg.getPayloadAsText())
                            .flatMap(payload -> handleMessage(session, payload, topic))
                            .doOnError(err -> System.err.println("WebSocket error: " + err.getMessage()))
                            .doOnTerminate(() -> System.out.println("WebSocket connection terminated"))
                            .then();

                    return sendMessages.then(receive);
                }
        ).subscribe();
    }


    private Mono<Void> handleMessage(org.springframework.web.reactive.socket.WebSocketSession session, String payload, String topic) {
        try {
            // Handle heartbeat
            // occurs only in MD subscriptions
//            if (payload.contains("\"method\":\"public/heartbeat\"")) {
//                Heartbeat parsed = objectMapper.readValue(payload, Heartbeat.class);
//                Heartbeat heartbeatResponse = Heartbeat.builder()
//                        .id(parsed.getId())
//                        .method("public/respond-heartbeat")
//                        .build();
//                return session.send(Mono.just(session.textMessage(heartbeatResponse.toJson()))).then(Mono.empty());
//            }
            // Handle authentication failure
            // fixed, but still want to keep these lines for the future
//            if (payload.contains("\"code\":\"40101\"")) {
//                ApiRequestJson authWsRequest = ApiRequestJson.builder()
//                        .id(1L)
//                        .method("public/auth")
//                        .build();
//                String requestBody = signAndParseToJsonString(authWsRequest, ApplicationProperties.getApiSecret());
//                return session.send(Mono.just(session.textMessage(requestBody))).then(Mono.empty());
//            }
            // Handle book data
            UserOrderResponse parsed = objectMapper.readValue(payload, UserOrderResponse.class);
            String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("[" + formatted + "] Parsed DTO:\n" + parsed);

            messagingTemplate.convertAndSend(topic, parsed);

        } catch (Exception e) {
            System.err.println("Failed to parse message:\n" + payload);
            e.printStackTrace();
        }
        return Mono.empty();
    }
}
