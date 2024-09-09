package com.practice_back.entity;

import com.practice_back.dto.CartDTO;
import com.practice_back.dto.CartItemDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CartTest {
    private String categoryTitle = "임시카테고리";
    private long cartId = 1L;
    private long itemId = 100L;
    private long itemPrice = 10;
    private String itemTitle = "아이템이름";
    private Long cartItemId = 10L;
    private int quantity = 11;
    private long totalPrice = 1000L;
    private String email = "kkk@gmail.com";
    private String password = "123";
    private String phoneNumber = "01055551111";
    private String picture = "url";
    private Authority authority = Authority.ROLE_USER;
    @DisplayName("빌더를 사용해 Cart 엔티티를 생성할 수 있다.")
    @Test
    void createCartEntity(){
        // Given
        List<CartItem> cartItems = new ArrayList<>();
        Member member = createMember();
        cartItems.add(createCartItem());

        Long cartID = 10L;
        // When
        Cart cart = Cart.builder()
                .id(cartID)
                .member(member)
                .cartItems(cartItems)
                .build();
        // Then
        assertEquals(cart.getId(), cartID);
        assertEquals(cart.getMember().getEmail(), email);
        assertEquals(cart.getMember().getPicture(), picture);
        assertEquals(cart.getMember().getPassword(), password);
        assertEquals(cart.getMember().getPhoneNumber(), phoneNumber);
        assertEquals(cart.getMember().getAuthority(), authority);

        CartItem cartItem = cart.getCartItems().get(0);
        assertEquals(cartItem.getId(),cartItemId);
        assertEquals(cartItem.getQuantity(), quantity);
        assertEquals(cartItem.getTotalPrice(), totalPrice);
        assertEquals(cartItem.getItems().getItemTitle(), itemTitle);
        assertEquals(cartItem.getItems().getItemPrice(), itemPrice);
        assertEquals(cartItem.getItems().getItemId(), itemId);
        assertEquals(cartItem.getCart().getId(), cartId);
    }
    @DisplayName("Cart엔티티를 CartDTO로 변경할 수 있다.")
    @Test
    void toDTO(){
        // Given
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(createCartItem());

        Long cartID = 10L;

        Cart cart = Cart.builder()
                .id(cartID)
                .cartItems(cartItems)
                .build();
        // When
        CartDTO dto = Cart.toDTO(cart);

        // Then
        CartItemDTO cartItemDTO = dto.getCartItemsDTO().get(0);

        assertEquals(cartItemDTO.getId(),cartItemId);
        assertEquals(cartItemDTO.getQuantity(), quantity);
        assertEquals(cartItemDTO.getTotalPrice(), totalPrice);
        assertEquals(cartItemDTO.getItemsDTO().getItemTitle(), itemTitle);
        assertEquals(cartItemDTO.getItemsDTO().getItemPrice(), itemPrice);
        assertEquals(cartItemDTO.getItemsDTO().getItemId(), itemId);
        assertEquals(cartItemDTO.getCartId(), cartId);
    }
    public Member createMember(){
        return Member.builder()
                .email(email)
                .picture(picture)
                .password(password)
                .phoneNumber(phoneNumber)
                .authority(authority)
                .deliveryAddresses(new ArrayList<>())
                .build();
    }
    public CartItem createCartItem(){
        Category category = Category.builder()
                .categoryTitle(categoryTitle)
                .build();

        Cart cart = Cart.builder()
                .id(cartId)
                .build();

        Items items = Items.builder()
                .itemId(itemId)
                .category(category)
                .itemPrice(itemPrice)
                .itemTitle(itemTitle)
                .build();

        return CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .items(items)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();

    }
}