package com.example.miniorder.domain.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.miniorder.domain.cart.CartItem;
import com.example.miniorder.domain.cart.CartItemRepository;
import com.example.miniorder.domain.cart.CartItemResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(OrderResponse::new).toList();
    }

    @Transactional
    public Long createOrder() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            cartItem.getProduct().decreaseStock(cartItem.getQuantity());
            return new OrderItem(cartItem);
        }).toList();

        Order saveOrder = orderRepository.save(new Order(orderItems));
        cartItemRepository.deleteAll();

        return saveOrder.getId();
    }

}
