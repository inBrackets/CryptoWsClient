package org.example.cryptowsclient.clock;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Random;

@Component
public class TimestampPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Random random = new Random();

    // Example data
    private final List<String> animals = List.of("Dog", "Cat", "Elephant", "Lion", "Tiger", "Penguin", "Giraffe");
    private final List<String> foods = List.of("Pizza", "Sushi", "Burger", "Pasta", "Salad", "Taco", "Ice Cream");

    public TimestampPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000) // every 1 second
    public void publishTimestamp() {
        String currentTime = LocalDateTime.now().format(formatter);
        messagingTemplate.convertAndSend("/subscribe/user.balance", currentTime);
        System.out.println("Timestamp published: " + currentTime);
    }

    @Scheduled(fixedRate = 500)
    public void publishRandomAnimal() {
        String animal = animals.get(random.nextInt(animals.size()));
        messagingTemplate.convertAndSend("/subscribe/random.animal", animal);
        System.out.println("Animal published: " + animal);
    }

    @Scheduled(fixedRate = 357)
    public void publishRandomFood() {
        String food = foods.get(random.nextInt(foods.size()));
        messagingTemplate.convertAndSend("/subscribe/random.food", food);
        System.out.println("Food published: " + food);
    }
}