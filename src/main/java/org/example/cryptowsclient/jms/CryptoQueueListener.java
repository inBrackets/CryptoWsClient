package org.example.cryptowsclient.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CryptoQueueListener {

    @JmsListener(destination = "crypto.market.feed")
    public void receive(String message) {
        System.out.println("Received from Artemis: " + message);
    }
}