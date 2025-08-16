package org.example.cryptowsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoWsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoWsClientApplication.class, args);
    }

}
