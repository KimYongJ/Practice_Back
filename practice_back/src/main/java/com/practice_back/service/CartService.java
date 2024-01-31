package com.practice_back.service;

import org.springframework.http.ResponseEntity;

public interface CartService {
    public ResponseEntity<Object> getCartByEmail();
    public ResponseEntity<Object> countCartItems();
    public ResponseEntity<Object> insertCartItem(Integer quantity, Long itemId);
    public ResponseEntity<Object> updateCartItem(Integer quantity, Long itemId);
    public ResponseEntity<Object> deleteCartItem(Long itemId);
}
