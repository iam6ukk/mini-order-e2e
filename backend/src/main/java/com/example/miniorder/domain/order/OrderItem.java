package com.example.miniorder.domain.order;

import com.example.miniorder.domain.cart.CartItem;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private Long productId;
    private String productName;
    private int orderPrice;
    private int quantity;

    protected OrderItem() {
    }

    public OrderItem(CartItem cartItem) {
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.orderPrice = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public int getTotalPrice() {
        return orderPrice * quantity;
    }

}
