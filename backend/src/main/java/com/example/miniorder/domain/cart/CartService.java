package com.example.miniorder.domain.cart;

import java.util.List;
import java.util.Optional;

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

    // 장바구니 아이템 조회
    @Transactional(readOnly = true)
    public List<CartItemResponse> findAll() {
        return cartItemRepository.findAll().stream().map(CartItemResponse::new).toList();
    }

    // 장바구니 아이템 추가
    @Transactional
    public CartItemResponse addItem(CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 같은 상품을 담을 경우 중복 추가
        Optional<CartItem> existing = cartItemRepository.findByProductId(request.getProductId());
        if (existing.isPresent()) {
            existing.get().addQuantity(request.getQuantity());
            cartItemRepository.save(existing.get());
            return new CartItemResponse(existing.get());
        }
        CartItem saved = cartItemRepository.save(new CartItem(product, request.getQuantity()));
        return new CartItemResponse(saved);
    }

    // 장바구니 아이템 개별 삭제
    @Transactional
    public void deleteItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다.");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    // 장바구니 아이템 전체 삭제
    @Transactional
    public void clear() {
        cartItemRepository.deleteAll();
    }

}
