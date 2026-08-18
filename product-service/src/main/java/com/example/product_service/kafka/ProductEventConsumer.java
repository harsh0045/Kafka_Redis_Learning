package com.example.product_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class ProductEventConsumer {

    @KafkaListener(
            topics = "product-events",
            groupId = "product-service-group",
            concurrency = "3"
    )
    public void consume(
            String message,
            Acknowledgment acknowledgment) {

        System.out.println("==============================");
        System.out.println(
                "THREAD: " + Thread.currentThread().getName()
        );
        System.out.println("KAFKA MESSAGE RECEIVED");
        System.out.println(message);
        System.out.println("==============================");

        if (message.contains("bad")) {
            throw new RuntimeException("Bad product event");
        }

        System.out.println("Processing successful");

        acknowledgment.acknowledge();

        System.out.println("Offset acknowledged");
    }
}