package com.example.miniorder.domain.cart;

import lombok.Getter;

@Getter
public class CartItemResponse {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final int price;
    private final int quantity;
    private final int totalPrice;

    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.price = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = cartItem.getTotalPrice();
    }

} 
