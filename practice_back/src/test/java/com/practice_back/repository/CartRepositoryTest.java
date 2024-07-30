package com.practice_back.repository;

import com.practice_back.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CartRepositoryTest {
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
    void tearDown(){
        cartItemRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        itemsRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }
    @DisplayName("사용자 ID를 이용해 카트에 담긴 아이템 숫자를 가져올 수 있다.")
    @Test
    void countItemsByMemberId(){
        // Given
        Category category = createTempCategory("카테고리1");
        Items item1 = createItem("사과",500L,"www.naver.com", category);
        Items item2 = createItem("바나나",400L,"www.google.com", category);
        Items item3 = createItem("메론",1400L,"www.kt.com", category);
        Items item4 = createItem("수박",1500L,"www.lg.com", category);
        itemsRepository.saveAll(List.of(item1, item2, item3, item4));
        Member member1 = createMember("dummy@gamil.com","123");
        memberRepository.save(member1);
        Cart cart = createCart(member1);
        cart.getCartItems().add(createCartItem(cart, item1));
        cart.getCartItems().add(createCartItem(cart, item2));
        cart.getCartItems().add(createCartItem(cart, item3));
        cart.getCartItems().add(createCartItem(cart, item4));
        cartRepository.save(cart);
        // When
        long cnt = cartRepository.countItemsByMemberId(member1.getId());
        // Then
        assertThat(cnt).isEqualTo(4);
    }
    @DisplayName("사용자의 ID를 이용해 객체를 가져올 수 있다.")
    @Test
    void findByMemberId(){
        // Given
        Member member1 = createMember("dummy@gamil.com","123");
        memberRepository.save(member1);
        Cart cart = createCart(member1);
        cartRepository.save(cart);
        // When
        Optional<Cart> result = cartRepository.findByMemberId(member1.getId());
        // Then
        assertThat(result).isPresent()
                .get()
                .satisfies(cartData ->{
                    assertThat(cartData.getMember()).isNotNull();
                    assertThat(cartData.getMember().getId()).isEqualTo(member1.getId());
                    assertThat(cartData.getMember().getId()).isNotEqualTo("123");
                        });
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
    public Category createTempCategory(String categoryStr){
        Category category = Category.builder()
                .categoryTitle(categoryStr)
                .build();
        return categoryRepository.save(category);
    }
}