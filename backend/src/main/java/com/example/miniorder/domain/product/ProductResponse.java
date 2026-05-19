package com.example.miniorder.domain.product;

import lombok.Getter;

@Getter
public class ProductResponse {
    private final Long id;
    private final String name;
    private final int price;
    private final int stockQuantity;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
    }
    
}
