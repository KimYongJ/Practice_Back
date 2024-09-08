package com.practice_back.entity;

import com.practice_back.dto.ItemsDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemsTest {
    @DisplayName("아이템 객체를 빌더를 통해 생성할 수 있다.")
    @Test
    void createItemsEntity(){
        // Given
        Long itemId         = 10L;
        Long itemPrice      = 100L;
        String imgUrl       = "url";
        String itemTitle    = "사과";
        String categoryTitle = "임시카테고리";
        Category category = createTempCategory(categoryTitle);
        // When
        Items items = Items.builder()
                .itemId(itemId)
                .itemPrice(itemPrice)
                .imgUrl(imgUrl)
                .itemTitle(itemTitle)
                .category(category)
                .build();
        // Then
        assertEquals(items.getItemId(), itemId);
        assertEquals(items.getItemPrice(), itemPrice);
        assertEquals(items.getImgUrl(), imgUrl);
        assertEquals(items.getItemTitle(), itemTitle);
        assertEquals(items.getCategory().getCategoryTitle(), categoryTitle);
    }
    @DisplayName("Items 객체를 ItemsDTO객체로 변경할 수 있다.")
    @Test
    void toDTO(){
        // Given
        Long itemId         = 10L;
        Long itemPrice      = 100L;
        String imgUrl       = "url";
        String itemTitle    = "사과";
        String categoryTitle = "임시카테고리";
        Category category = createTempCategory(categoryTitle);
        Items items = Items.builder()
                .itemId(itemId)
                .itemPrice(itemPrice)
                .imgUrl(imgUrl)
                .itemTitle(itemTitle)
                .category(category)
                .build();
        // When
        ItemsDTO itemsDTO = Items.toDTO(items);
        // Then
        assertEquals(itemsDTO.getItemId(), itemId);
        assertEquals(itemsDTO.getItemPrice(), itemPrice);
        assertEquals(itemsDTO.getImgUrl(), imgUrl);
        assertEquals(itemsDTO.getItemTitle(), itemTitle);
        assertEquals(itemsDTO.getCategoryDTO().getCategoryTitle(), categoryTitle);
    }
    public Category createTempCategory(String categoryTitle){
        Category category = Category.builder()
                .categoryTitle(categoryTitle)
                .build();
        return category;
    }
}