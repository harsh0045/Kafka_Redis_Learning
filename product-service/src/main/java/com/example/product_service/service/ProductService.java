package com.example.product_service.service;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.InsufficientStockException;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.repository.ProductRepository;

import com.example.product_service.specification.ProductSpecification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        Product savedProduct = productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    // Pagination + Sorting
    public Page<ProductResponse> getAllProducts(
            String name,
            Double minPrice,
            Double maxPrice,
            Pageable pageable) {

        Specification<Product> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction();

        if (name != null && !name.isBlank()) {

            specification = specification.and(
                    ProductSpecification.nameContains(name)
            );
        }

        if (minPrice != null) {

            specification = specification.and(
                    ProductSpecification.priceGreaterThanOrEqual(minPrice)
            );
        }

        if (maxPrice != null) {

            specification = specification.and(
                    ProductSpecification.priceLessThanOrEqual(maxPrice)
            );
        }

        return productRepository
                .findAll(specification, pageable)
                .map(this::convertToResponse);
    }
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {
        System.out.println("========== DATABASE CALL ==========");
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        return convertToResponse(product);
    }

    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());

        Product updatedProduct =
                productRepository.save(existingProduct);

        return convertToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        productRepository.delete(existingProduct);
    }


    @Transactional
    public ProductResponse purchaseProduct(
            Long id,
            Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        if (product.getQuantity() < quantity) {

            throw new InsufficientStockException(
                    "Not enough stock available. Available: "
                            + product.getQuantity()
                            + ", Requested: "
                            + quantity
            );
        }

        product.setQuantity(
                product.getQuantity() - quantity
        );

        Product updatedProduct =
                productRepository.save(product);

        return convertToResponse(updatedProduct);
    }

    private ProductResponse convertToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}