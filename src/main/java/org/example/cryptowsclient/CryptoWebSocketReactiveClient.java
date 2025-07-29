package org.example.cryptowsclient;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cryptowsclient.dto.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.WebSocketSession;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.time.Duration;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.net.URI;

@Component
public class CryptoWebSocketReactiveClient {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final String subscriptionMessage = """
        {
          "id": 1,
          "method": "subscribe",
          "params": {
            "channels": ["book.CRO_USDT.10"]
          }
        }
        """;

    @PostConstruct
    public void connect() {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(
                URI.create("wss://stream.crypto.com/exchange/v1/market"),
                this::handleSession
        ).subscribe();
    }

    private Mono<Void> handleSession(WebSocketSession session) {
        // Send the initial subscribe message
        Mono<Void> send = session.send(
                Mono.just(session.textMessage(subscriptionMessage))
        );

        // Receive messages
        Flux<Void> receiveAndProcess = session.receive()
                .map(msg -> msg.getPayloadAsText())
                .flatMap(this::processMessage)
                .onErrorContinue((err, obj) -> err.printStackTrace());

        return send.thenMany(receiveAndProcess).then();
    }

    private Mono<Void> processMessage(String message) {
        try {
            WebSocketMessage ws = objectMapper.readValue(message, WebSocketMessage.class);

            if ("public/heartbeat".equals(ws.getMethod())) {
                WebSocketMessage heartbeatResponse = new WebSocketMessage();
                heartbeatResponse.setId(ws.getId());
                heartbeatResponse.setMethod("public/respond-heartbeat");

                String response = objectMapper.writeValueAsString(heartbeatResponse);
                return sendMessageToSocket(response); // async response
            }

            // Send message to Artemis
            jmsTemplate.convertAndSend("crypto.market.feed", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.empty();
    }

    // This holds a reference to the WebSocket session to send messages later
    private WebSocketSession liveSession;

    private Mono<Void> sendMessageToSocket(String message) {
        if (liveSession == null) return Mono.empty();
        return liveSession.send(Mono.just(liveSession.textMessage(message))).then();
    }
}