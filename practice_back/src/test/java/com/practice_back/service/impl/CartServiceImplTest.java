package com.practice_back.service.impl;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.dto.CartDTO;
import com.practice_back.dto.CartItemDTO;
import com.practice_back.entity.*;
import com.practice_back.repository.*;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @BeforeEach
    void setUp(){
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
    }
    @AfterEach
    void tearDwon(){
        cartItemRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        itemsRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }
    @DisplayName("인증된 사용자는 카트아이템을 가져올 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void getCartByEmail(){
        // Given When
        ResponseEntity<Object> result = cartServiceImpl.getCartByEmail();
        // Then
        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(), HttpStatus.OK);

                            Message msg = (Message)result.getBody();
                            assertEquals(msg.getMessage(),"성공");
                            assertEquals(msg.getStatus(), ErrorType.OK);

                            CartDTO cartDto = (CartDTO)msg.getData();
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
                        });
    }
    @DisplayName("인증된 사용자는 카트에 담긴 아이템의 개수를 가져올 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void countCartItems(){
        // Given When
        ResponseEntity<Object> result = cartServiceImpl.countCartItems();

        // Then
        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(), HttpStatus.OK);

                            Message msg = (Message)result.getBody();
                            assertEquals(msg.getMessage(),"성공");
                            assertEquals(msg.getStatus(), ErrorType.OK);

                            long cnt  = (long)msg.getData();
                            assertEquals(cnt,4L);
                        });
    }
    @DisplayName("수량과 아이템아이디를 입력하면 상품이 추가 된다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void insertCartItem(){
        // Given
        int qt = 5;
        long price = 2500L;
        Category category = createTempCategory("카테고리1");
        Items item = createItem("임시아이템",price,"www.google.com", category);
        itemsRepository.save(item);
        Long itemId = item.getItemId();
        // When
        ResponseEntity<Object> result1 = cartServiceImpl.insertCartItem(qt,itemId);
        ResponseEntity<Object> result2 = cartServiceImpl.getCartByEmail();
        // Then
        assertThat(result1).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);

                            Message msg = (Message)res.getBody();
                            assertEquals(msg.getStatus(),ErrorType.OK);
                            assertEquals(msg.getMessage(),"카트에 추가하였습니다.");
                        });
        assertThat(result2).isNotNull()
                        .satisfies(res->{
                            Object data = ((Message)res.getBody()).getData();
                            List<CartItemDTO> items = ((CartDTO)data).getCartItemsDTO();
                            assertThat(items).hasSize(5)
                                    .extracting("quantity","totalPrice")
                                    .contains(tuple(5,qt*price));
                        });
    }
    @DisplayName("수량과 아이템아이디를 입력하면 상품 수량과 총 가격을 수정할 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void updateCartItem(){
        // Given
        int qt = 5;
        int transQt = 10;
        long price = 2500L;
        Category category = createTempCategory("카테고리1");
        Items item = createItem("임시아이템",price,"www.google.com", category);
        itemsRepository.save(item);
        Long itemId = item.getItemId();

        // When
        cartServiceImpl.insertCartItem(qt,itemId);

        ResponseEntity<Object> result1 = cartServiceImpl.updateCartItem(transQt, itemId);
        ResponseEntity<Object> result2 = cartServiceImpl.getCartByEmail();

        // Then
        assertThat(result1).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);

                            Message msg = (Message)res.getBody();
                            assertEquals(msg.getStatus(),ErrorType.OK);
                            assertEquals(msg.getMessage(),ErrorType.OK.getErrStr());
                        });
        assertThat(result2).isNotNull()
                        .satisfies(res->{
                            Object data = ((Message)res.getBody()).getData();
                            List<CartItemDTO> items = ((CartDTO)data).getCartItemsDTO();
                            assertThat(items).hasSize(5)
                                    .extracting("quantity","totalPrice")
                                    .contains(tuple(transQt,transQt*price));
                        });
    }
    @DisplayName("아이템 아이디로 카트에 담긴 상품을 삭제할 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void test(){
        // Given
        int qt = 10;
        long price = 2500L;
        Category category = createTempCategory("카테고리1");
        Items item = createItem("임시아이템",price,"www.google.com", category);
        itemsRepository.save(item);
        Long itemId = item.getItemId();
        cartServiceImpl.insertCartItem(qt,itemId);
        // When
        List<CartItemDTO> origin = ((CartDTO)((Message)cartServiceImpl.getCartByEmail().getBody()).getData()).getCartItemsDTO();
        cartServiceImpl.deleteCartItem((itemId));
        List<CartItemDTO> updated = ((CartDTO)((Message)cartServiceImpl.getCartByEmail().getBody()).getData()).getCartItemsDTO();
        // Then
        assertThat(origin).hasSize(5)
                .extracting("quantity","totalPrice")
                .contains(tuple(qt, qt*price));
        assertThat(updated).hasSize(4)
                .extracting("quantity","totalPrice")
                .doesNotContain(tuple(qt, qt*price));
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