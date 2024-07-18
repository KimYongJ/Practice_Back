package com.practice_back.repository;

import com.practice_back.entity.Cart;
import com.practice_back.entity.Category;
import com.practice_back.entity.Items;
import com.practice_back.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    CategoryRepository categoryRepository;
    @AfterEach
    void tearDwon(){
        cartItemRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
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
    public Cart createCart(Member member){
        return Cart.builder()
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
                .categoryId(1L)
                .categoryTitle("임시카테고리")
                .build();
        return categoryRepository.save(category);

    }
}