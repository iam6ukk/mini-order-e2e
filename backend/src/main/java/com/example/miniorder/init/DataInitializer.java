package com.example.miniorder.init;

import org.springframework.stereotype.Component;

import com.example.miniorder.domain.product.Product;
import com.example.miniorder.domain.product.ProductRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() {
        if (productRepository.count() > 0) {
            return;
        }

        productRepository.save(new Product("노트북", 1200000, 10));
        productRepository.save(new Product("키보드", 80000, 20));
        productRepository.save(new Product("마우스", 30000, 30));
    }
}
