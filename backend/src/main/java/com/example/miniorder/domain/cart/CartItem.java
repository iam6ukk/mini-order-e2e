package com.example.miniorder.domain.cart;

import com.example.miniorder.domain.product.Product;

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
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private int quantity;

    protected CartItem() {
    }

    public CartItem(Product product, int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        this.product = product;
        this.quantity = quantity;
    }

    // 장바구니 수량 증가
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public int getTotalPrice() {
        return product.getPrice() * quantity;

    }

}
