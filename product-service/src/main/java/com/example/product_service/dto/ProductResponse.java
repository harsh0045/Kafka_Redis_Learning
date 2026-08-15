package com.example.product_service.dto;

import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private Double price;
    private Integer quantity;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String name,
            Double price,
            Integer quantity) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}