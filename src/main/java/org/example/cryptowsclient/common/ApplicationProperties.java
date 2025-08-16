package org.example.cryptowsclient.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Getter
    private static String apiKey;
    @Getter
    private static String apiSecret;

    @Value("${api.key}")
    public void setApiKey(String secret) {
        ApplicationProperties.apiKey = secret;
    }

    @Value("${api.secret}")
    public void setApiSecret(String secret) {
        ApplicationProperties.apiSecret= secret;
    }
}
