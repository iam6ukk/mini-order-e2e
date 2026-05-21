package com.example.miniorder.domain.cart;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/cart/items")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartItemResponse addItem(@Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(request);
    }
    
    @GetMapping
    public List<CartItemResponse> findAll() {
        return cartService.findAll();
    }
}
