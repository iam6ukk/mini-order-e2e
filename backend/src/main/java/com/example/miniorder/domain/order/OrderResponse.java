package com.example.miniorder.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class OrderResponse {
    private final Long id;
    private final int totalPrice;
    private final OrderStatus status;
    private final LocalDateTime createdAt;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.orderItems = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::new)
                .toList();
    }
}
