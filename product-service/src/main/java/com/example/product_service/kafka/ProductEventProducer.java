package com.example.product_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductEventProducer {

    private static final String TOPIC = "product-events";

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductEventProducer(
            KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductCreatedEvent(ProductCreatedEvent event) {

        kafkaTemplate.send(
                "product-events",
                event.getProductId().toString(),
                event
        );
    }
}