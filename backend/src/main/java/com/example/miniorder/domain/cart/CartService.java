package com.example.miniorder.domain.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.miniorder.domain.product.Product;
import com.example.miniorder.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartItemResponse addItem(CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        CartItem saved = cartItemRepository.save(new CartItem(product, request.getQuantity()));
        return new CartItemResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findAll() {
        return cartItemRepository.findAll().stream().map(CartItemResponse::new).toList();
    }    
}
