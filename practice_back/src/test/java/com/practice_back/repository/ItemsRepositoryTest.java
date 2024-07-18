package com.practice_back.repository;

import com.practice_back.entity.Category;
import com.practice_back.entity.Items;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest()
@Transactional
class ItemsRepositoryTest {

    @Autowired
    ItemsRepository itemsRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @AfterEach
    void tearDown(){
        itemsRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @DisplayName("아이템ID로 아이템을 가져올 수 있다.")
    @Test
    void findByItemId(){
        // Given
        Category category = createTempCategory();
        Items item1 = createItem("사과",500L,"www.naver.com",category);
        Items item2 = createItem("배",1500L,"naver",category);
        itemsRepository.save(item1);
        itemsRepository.save(item2);
        // When
        Items result1 = itemsRepository.findByItemId(1L);
        Items result2 = itemsRepository.findByItemId(2L);
        // Then
        assertThat(result1)
                .isNotNull()
                .extracting("itemId","itemTitle","itemPrice","imgUrl")
                .containsExactly(1L, "사과", 500L, "www.naver.com");
        assertThat(result2)
                .isNotNull()
                .extracting("itemId","itemTitle","itemPrice","imgUrl")
                .containsExactly(2L, "배", 1500L, "naver");
    }

    @DisplayName("가격을 이용해 아이템을 가져올 수 있다")
    @Test
    void findItemsByDynamicCondition(){
        // Given
        Category category = createTempCategory();
        Items item1 = createItem("사과",500L,"www.naver.com", category);
        Items item2 = createItem("바나나",400L,"www.google.com", category);
        Items item3 = createItem("메론",1400L,"www.kt.com", category);
        Items item4 = createItem("수박",1500L,"www.lg.com", category);
        itemsRepository.saveAll(List.of(item1, item2, item3, item4));
        // When
        Pageable pageable = PageRequest.of(0,10);
        // Then
        Page<Items> result1 = itemsRepository.findItemsByDynamicCondition(
                null,
                List.of(category.getCategoryId()),
                null,
                100L,
                500L,
                pageable
        );
        Page<Items> result2 = itemsRepository.findItemsByDynamicCondition(
                null,
                List.of(category.getCategoryId()),
                null,
                100L,
                1500L,
                pageable
        );

        assertThat(result1)
                .hasSize(2)
                .extracting("itemTitle","itemPrice","imgUrl")
                .containsExactlyInAnyOrder(
                        tuple("사과",500L,"www.naver.com"),
                        tuple("바나나",400L,"www.google.com")
                );
        assertThat(result2)
                .hasSize(4)
                .extracting("itemTitle","itemPrice","imgUrl")
                .containsExactlyInAnyOrder(
                        tuple("사과",500L,"www.naver.com"),
                        tuple("바나나",400L,"www.google.com"),
                        tuple("메론",1400L,"www.kt.com"),
                        tuple("수박",1500L,"www.lg.com")
                );

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