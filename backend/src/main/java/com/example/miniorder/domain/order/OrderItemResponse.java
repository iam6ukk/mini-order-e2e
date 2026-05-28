package com.example.miniorder.domain.order;

import lombok.Getter;

@Getter
public class OrderItemResponse {
    private final String productName;
    private final int orderPrice;
    private final int quantity;
    private final int totalPrice;

    public OrderItemResponse(OrderItem orderItem) {
        this.productName = orderItem.getProductName();
        this.orderPrice = orderItem.getOrderPrice();
        this.quantity = orderItem.getQuantity();
        this.totalPrice = orderItem.getTotalPrice();
    }
}
