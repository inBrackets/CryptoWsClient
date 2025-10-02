package org.example.cryptowsclient.orderhistory.ws;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.cryptowsclient.book.dto.Heartbeat;
import org.example.cryptowsclient.common.ApiRequestJson;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.example.cryptowsclient.common.ApplicationProperties;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.cryptowsclient.auth.SigningUtil.signAndParseToJsonString;

@Component
@Slf4j
public class UserWebSocketClient {

    private static final String WS_URL = "wss://stream.crypto.com/exchange/v1/user";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpMessagingTemplate messagingTemplate;
    private final ApplicationEventPublisher eventPublisher;

    private final AtomicBoolean connected = new AtomicBoolean(false);

    public UserWebSocketClient(SimpMessagingTemplate messagingTemplate, ApplicationEventPublisher eventPublisher) {
        this.messagingTemplate = messagingTemplate;
        this.eventPublisher = eventPublisher;

        Hooks.onErrorDropped(e -> {
            log.error("Dropped error: {}", e.getMessage());
        });
    }

    public void connect(String topic) {
        attemptConnection(buildInitialMessages(), topic, 1);
    }

    private void attemptConnection(List<String> initialMessages, String topic, int attempt) {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(
                URI.create(WS_URL),
                session -> {
                    connected.set(true);
                    log.info("✅ WebSocket connected (attempt {})", attempt);

                    // Send all initial messages (auth, then subscribe)
                    Mono<Void> sendMessages = Mono.delay(Duration.ofSeconds(1))
                            .thenMany(Flux.fromIterable(initialMessages))
                            .map(session::textMessage)
                            .as(session::send);

                    Mono<Void> receive = session.receive()
                            .map(msg -> msg.getPayloadAsText())
                            .flatMap(payload -> handleMessage(session, payload, topic))
                            .doOnError(err -> log.error("WebSocket error: {}", err.getMessage()))
                            .doOnTerminate(() -> {
                                connected.set(false);
                                log.info("⚠️ WebSocket connection terminated");
                                scheduleReconnect(topic, attempt + 1);
                            })
                            .then();

                    return sendMessages.then(receive);
                }
        ).subscribe();
    }

    private void scheduleReconnect(String topic, int attempt) {
        int delaySeconds = Math.min(60, (int) Math.pow(2, attempt));
        log.info("⏳ Reconnecting in {}s (attempt {})", delaySeconds, attempt);
        Mono.delay(Duration.ofSeconds(delaySeconds))
                .subscribe(t -> attemptConnection(buildInitialMessages(), topic, attempt));
    }

    /**
     * Builds fresh auth message with a new nonce each time.
     */
    private List<String> buildInitialMessages() {
        try {
            ApiRequestJson authRequest = ApiRequestJson.builder()
                    .id(1L)
                    .method("public/auth")
                    .apiKey(ApplicationProperties.getApiKey())
                    .build();

            String authMessage = signAndParseToJsonString(authRequest, ApplicationProperties.getApiSecret());

            return List.of(authMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build initial auth message", e);
        }
    }

    private Mono<Void> handleMessage(org.springframework.web.reactive.socket.WebSocketSession session,
                                     String payload,
                                     String topic) {
        log.info("{} :: Received payload: {}", Instant.now(), payload);
        try {
            // heartbeat
            if (payload.contains("\"method\":\"public/heartbeat\"")) {
                Heartbeat parsed = objectMapper.readValue(payload, Heartbeat.class);
                Heartbeat heartbeatResponse = Heartbeat.builder()
                        .id(parsed.getId())
                        .method("public/respond-heartbeat")
                        .build();
                log.info("{} :: Sending heartbeat response: {}", Instant.now(), heartbeatResponse.toJson());
                return session.send(Mono.just(session.textMessage(heartbeatResponse.toJson()))).then();
            }
            // successful auth
            else if (payload.contains("\"method\":\"public/auth\"") && payload.contains("\"code\":0")) {
                ApiRequestJson subscribeRequest = ApiRequestJson.builder()
                        .id(2L)
                        .method("subscribe")
                        .params(Map.of("channels", List.of("user.order")))
                        .build();

                String subscribeMessage = objectMapper.writeValueAsString(subscribeRequest);
                return session.send(Mono.just(session.textMessage(subscribeMessage))).then();
            }
            // auth failed (e.g. invalid nonce)
            else if (payload.contains("\"method\":\"public/auth\"") && payload.contains("\"code\":40102")) {
                log.error("❌ Authentication failed: Invalid nonce. Full payload: {}", payload);
                // close connection to trigger reconnect
                return session.close();
            }

            // order data
            ApiResponseDto<ApiResultDto<OrderItemDto>> parsed =
                    objectMapper.readValue(payload, new TypeReference<ApiResponseDto<ApiResultDto<OrderItemDto>>>() {});

            if (parsed.getResult() == null) {
                return Mono.empty(); // skip ack messages with no data
            }

            String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("[{}] Parsed DTO:\n{}", formatted, parsed);

            messagingTemplate.convertAndSend(topic, parsed);
            eventPublisher.publishEvent(new OrderMessageEvent(parsed));

        } catch (Exception e) {
            log.error("❌ Failed to parse message:\n{}", payload);
            e.printStackTrace();
        }
        return Mono.empty();
    }

    public boolean isConnected() {
        return connected.get();
    }
}
