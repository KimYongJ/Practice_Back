package com.practice_back.repository;

import com.practice_back.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CartItemRepositoryTest {
    @PersistenceContext
    EntityManager entityManager;
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
        memberRepository.deleteAllInBatch();
        itemsRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }
    @DisplayName("카트 ID와 아이템 ID로 해당 상품이 카트에 담겨있는지 확인할 수 있다")
    @Test
    void existsByCartIdAndItemsItemId(){
        // Given
        Category category = createTempCategory();
        Items item1 = createItem("사과",500L,"www.naver.com", category);
        Items item2 = createItem("바나나",400L,"www.google.com", category);
        Items item3 = createItem("수박",300L,"www.nate.com", category);
        Items res_item1 = itemsRepository.save(item1);
        Items res_item2 = itemsRepository.save(item2);
        Member member1 = createMember("dummy@gamil.com","123");
        memberRepository.save(member1);
        Cart cart = createCart(member1);
        cart.getCartItems().add(createCartItem(cart, item1,1,100L));
        cart.getCartItems().add(createCartItem(cart, item2, 2, 200L));
        Cart res_cart = cartRepository.save(cart);
        // When
        boolean result1 = cartItemRepository.existsByCartIdAndItemsItemId(res_cart.getId(), res_item1.getItemId());
        boolean result2 = cartItemRepository.existsByCartIdAndItemsItemId(res_cart.getId(), res_item2.getItemId());
        boolean result3 = cartItemRepository.existsByCartIdAndItemsItemId(res_cart.getId(), item3.getItemId());
        // Then
        assertThat(res_item1 == item1).isTrue(); // 저장시 입력한 객체와 출력된 객체가 동일 주소값을 참조함을 증명
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
    }
    @DisplayName("Cart Id와 Item Id로 상품 수량을 변경할 수 있다")
    @Test
    void updateTotalPriceQuantityByCartIdAndItemId(){
        // Given
        Category category = createTempCategory();
        Items item1 = createItem("사과",500L,"www.naver.com", category);
        Items item2 = createItem("바나나",400L,"www.google.com", category);
        Items res_item1 = itemsRepository.save(item1);
        Items res_item2 = itemsRepository.save(item2);

        Member member1 = createMember("dummy@gamil.com","123");
        memberRepository.save(member1);
        Cart cart = createCart(member1);
        cart.getCartItems().add(createCartItem(cart, item1,1,100L));
        cart.getCartItems().add(createCartItem(cart, item2, 2, 200L));
        Cart res_cart = cartRepository.save(cart);

        // When
        int updateCnt1 = cartItemRepository.updateTotalPriceQuantityByCartIdAndItemId(1000L, 5, res_cart.getId(), res_item1.getItemId());
        int updateCnt2 = cartItemRepository.updateTotalPriceQuantityByCartIdAndItemId(2000L, 10, res_cart.getId(), res_item2.getItemId());

        // 위의 jpql사용시 동일 트랜잭션 내에서 같은 엔티티를 여러 번 조회할 때 데이터베이스에서 직접 조회하지 않고 영속성 컨텍스트에서 가져옴
        // JPQL이나 Native쿼리 사용해 데이터베이스를 직접 변경하면 영속성 컨텍스트가 해당 변경 사항을 인지하지 못함, 하여 아래 2줄 추가
        entityManager.flush(); // 영속성 컨텍스트의 변경 사항을 데이터베이스에 반영
        entityManager.clear(); // 영속성 컨텍스트를 비워 이 후 데이터 조회 시 데이터베이스에서 직접 가져오도록함

        Optional<Cart> result = cartRepository.findByMemberId(member1.getId());
        // Then
        assertThat(updateCnt1).isOne();
        assertThat(updateCnt2).isOne();
        assertThat(result)
                .isPresent()
                .get()
                .satisfies(cartData ->{
                    assertThat(cartData.getCartItems()).isNotNull();
                    assertThat(cartData.getCartItems().size()).isEqualTo(2);
                    assertThat(cartData.getCartItems().get(0).getQuantity()).isEqualTo(5);
                    assertThat(cartData.getCartItems().get(0).getTotalPrice()).isEqualTo(1000L);
                    assertThat(cartData.getCartItems().get(1).getQuantity()).isEqualTo(10);
                    assertThat(cartData.getCartItems().get(1).getTotalPrice()).isEqualTo(2000L);
                });
    }
    public CartItem createCartItem(Cart cart, Items item,int qt, long price){
        return CartItem.builder()
                .cart(cart)
                .items(item)
                .quantity(qt)
                .totalPrice(price)
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
    public Category createTempCategory(){
        Category category = Category.builder()
                .categoryTitle("임시카테고리")
                .build();
        return categoryRepository.save(category);
    }
}