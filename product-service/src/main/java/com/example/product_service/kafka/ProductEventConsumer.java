package com.example.product_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventConsumer {
    @KafkaListener(
            topics = "product-events",
            groupId = "product-service-group"
    )
    public void consume(String event) {

        System.out.println("=================================");
        System.out.println("KAFKA MESSAGE RECEIVED");
        System.out.println(event);
        System.out.println("=================================");

        if (event.contains("bad")) {
            throw new RuntimeException("Bad product event");
        }

        System.out.println("✅ Product event processed successfully");
    }
}