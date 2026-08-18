package com.example.product_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventConsumer {

    @KafkaListener(
            topics = "product-events",
            groupId = "test-group"
    )
    public void consume(String message) {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("Message: " + message);
        System.out.println("=================================");
    }
}