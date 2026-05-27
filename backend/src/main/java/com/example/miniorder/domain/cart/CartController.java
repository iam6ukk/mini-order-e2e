package com.example.miniorder.domain.cart;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/cart/items")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 아이템 조회
    @GetMapping
    public List<CartItemResponse> findAll() {
        return cartService.findAll();
    }

    // 장바구니 아이템 추가
    @PostMapping
    public CartItemResponse addItem(@Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(request);
    }

    // 장바구니 아이템 개별 삭제
    @DeleteMapping("/{cartItemId}")
    public void deleteItem(@PathVariable Long cartItemId) {
        cartService.deleteItem(cartItemId);
    }

    // 장바구니 아이템 전체 삭제
    @DeleteMapping
    public void clear() {
        cartService.clear();
    }

}
