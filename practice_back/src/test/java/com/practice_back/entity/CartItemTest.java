package com.practice_back.entity;

import com.practice_back.dto.CartItemDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class CartItemTest {
    @DisplayName("빌더를 통해 카트아이템 객체를 생성할 수 있다.")
    @Test
    void createCartItemEntity(){
        // Given
        String categoryTitle = "임시카테고리";
        Category category = Category.builder()
                .categoryTitle(categoryTitle)
                .build();

        long cartId = 1L;
        Cart cart = Cart.builder()
                .id(cartId)
                .build();
        long itemId = 100L;
        long itemPrice = 10;
        String itemTitle = "아이템이름";
        Items items = Items.builder()
                .itemId(itemId)
                .category(category)
                .itemPrice(itemPrice)
                .itemTitle(itemTitle)
                .build();
        Long cartItemId = 10L;
        int quantity = 11;
        long totalPrice = 1000L;
        // When
        CartItem cartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .items(items)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
        // Then
        assertEquals(cartItem.getId(),cartItemId);
        assertEquals(cartItem.getQuantity(), quantity);
        assertEquals(cartItem.getTotalPrice(), totalPrice);
        assertEquals(cartItem.getItems().getItemTitle(), itemTitle);
        assertEquals(cartItem.getItems().getItemPrice(), itemPrice);
        assertEquals(cartItem.getItems().getItemId(), itemId);
        assertEquals(cartItem.getCart().getId(), cartId);
    }
    @DisplayName("CartItem엔티티를 DTO로 변경할 수 있다.")
    @Test
    void toDTO(){
        // Given
        String categoryTitle = "임시카테고리";
        Category category = Category.builder()
                .categoryTitle(categoryTitle)
                .build();

        long cartId = 1L;
        Cart cart = Cart.builder()
                .id(cartId)
                .build();
        long itemId = 100L;
        long itemPrice = 10;
        String itemTitle = "아이템이름";
        Items items = Items.builder()
                .category(category)
                .itemId(itemId)
                .itemPrice(itemPrice)
                .itemTitle(itemTitle)
                .build();
        Long cartItemId = 10L;
        int quantity = 11;
        long totalPrice = 1000L;

        CartItem cartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .items(items)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();

        // When
        CartItemDTO dto = CartItem.toDTO(cartItem);

        // Then
        assertEquals(dto.getId(),cartItemId);
        assertEquals(dto.getQuantity(), quantity);
        assertEquals(dto.getTotalPrice(), totalPrice);
        assertEquals(dto.getItemsDTO().getItemTitle(), itemTitle);
        assertEquals(dto.getItemsDTO().getItemPrice(), itemPrice);
        assertEquals(dto.getItemsDTO().getItemId(), itemId);
        assertEquals(dto.getCartId(), cartId);
    }
}