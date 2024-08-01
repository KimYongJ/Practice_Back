package com.practice_back.service.impl;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.dto.CartDTO;
import com.practice_back.dto.CartItemDTO;
import com.practice_back.entity.*;
import com.practice_back.repository.*;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CartServiceImplTest {
    @Autowired
    CartServiceImpl cartServiceImpl;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @AfterEach
    void tearDwon(){
        cartItemRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        itemsRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }
    @DisplayName("인증된 사용자는 카트에 담긴 아이템의 개수를 가져올 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void countCartItems(){
        // Given
        Category category = createTempCategory("카테고리1");
        Items item1 = createItem("사과",500L,"www.naver.com", category);
        Items item2 = createItem("바나나",400L,"www.google.com", category);
        Items item3 = createItem("메론",1400L,"www.kt.com", category);
        itemsRepository.saveAll(List.of(item1, item2, item3));
        Member member1 = createMember("kyj","1");
        memberRepository.save(member1);
        Cart cart = createCart(member1);
        cart.getCartItems().add(createCartItem(cart, item1));
        cart.getCartItems().add(createCartItem(cart, item2));
        cart.getCartItems().add(createCartItem(cart, item3));
        cartRepository.save(cart);
        // When
        ResponseEntity<Object> result = cartServiceImpl.countCartItems();
        Message resultBody = (Message)result.getBody();
        long cnt  = (long)resultBody.getData();
        // Then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(resultBody.getMessage(),"성공");
        assertEquals(resultBody.getStatus(), ErrorType.OK);
        assertEquals(cnt,3L);
    }
    @DisplayName("인증된 사용자는 카트아이템을 가져올 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void getCartByEmail(){
        // Given
        Category category = createTempCategory("카테고리1");
        Items item1 = createItem("사과",500L,"www.naver.com", category);
        Items item2 = createItem("바나나",400L,"www.google.com", category);
        Items item3 = createItem("메론",1400L,"www.kt.com", category);
        Items item4 = createItem("수박",1500L,"www.lg.com", category);
        itemsRepository.saveAll(List.of(item1, item2, item3, item4));
        Member member1 = createMember("kyj","1");
        memberRepository.save(member1);
        Cart cart = createCart(member1);
        cart.getCartItems().add(createCartItem(cart, item1));
        cart.getCartItems().add(createCartItem(cart, item2));
        cart.getCartItems().add(createCartItem(cart, item3));
        cart.getCartItems().add(createCartItem(cart, item4));
        cartRepository.save(cart);
        // When
        ResponseEntity<Object> result = cartServiceImpl.getCartByEmail();
        Message resultBody = (Message)result.getBody();
        CartDTO cartDto = (CartDTO)resultBody.getData();
        // Then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(resultBody.getMessage(),"성공");
        assertEquals(resultBody.getStatus(), ErrorType.OK);
        assertThat(cartDto.getCartItemsDTO())
                .hasSize(4)
                .extracting(CartItemDTO::getItemsDTO)
                .extracting("itemTitle", "itemPrice", "imgUrl")
                .containsExactlyInAnyOrder(
                        tuple("사과",500L,"www.naver.com"),
                        tuple("바나나",400L,"www.google.com"),
                        tuple("메론",1400L,"www.kt.com"),
                        tuple("수박",1500L,"www.lg.com")
                );
    }
    public CartItem createCartItem(Cart cart, Items item){
        return CartItem.builder()
                .cart(cart)
                .items(item)
                .quantity(1)
                .totalPrice(100)
                .build();
    }
    public Cart createCart(Member member){
        return Cart.builder()
                .cartItems(new ArrayList<>())
                .member(member)
                .build();
    }
    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .password(pwd)
                .build();
    }
    public Items createItem(String title, Long price, String imgUrl, Category category){
        return Items.builder()
                .itemTitle(title)
                .itemPrice(price)
                .imgUrl(imgUrl)
                .category(category)
                .build();
    }
    public Category createTempCategory(String categoryTitle){
        Category category = Category.builder()
                .categoryTitle(categoryTitle)
                .build();
        return categoryRepository.save(category);
    }
}