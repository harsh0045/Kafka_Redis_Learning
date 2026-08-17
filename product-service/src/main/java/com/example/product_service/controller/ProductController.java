package com.example.product_service.controller;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request) {

        return productService.createProduct(request);
    }

    @PostMapping("/{id}/purchase")
    public ProductResponse purchaseProduct(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        return productService.purchaseProduct(
                id,
                quantity
        );
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            Double minPrice,

            @RequestParam(required = false)
            Double maxPrice,

            Pageable pageable) {

        return productService.getAllProducts(
                name,
                minPrice,
                maxPrice,
                pageable
        );
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @PathVariable Long id) {

        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return "Product deleted successfully";
    }
}