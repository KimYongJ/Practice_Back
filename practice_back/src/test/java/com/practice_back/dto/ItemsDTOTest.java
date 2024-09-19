package com.practice_back.dto;

import com.practice_back.entity.Items;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class ItemsDTOTest {
    @DisplayName("빌더를 통해 ItemsDTO객체를 생성할 수 있다.")
    @Test
    void testItemsDTO(){
        // Given
        Long itemId             = 10L;
        String imgUrl           = "IMG_URL";
        String itemTitle        = "아이템이름";
        Long itemPrice          = 1000L;
        Long categoryId         = 11L;
        String categoryTitle    = "카테고리명";
        CategoryDTO categoryDTO = CategoryDTO
                                        .builder()
                                        .categoryId(categoryId)
                                        .categoryTitle(categoryTitle)
                                        .build();

        // When
        ItemsDTO itemsDTO = ItemsDTO.builder()
                .itemId(itemId)
                .imgUrl(imgUrl)
                .itemTitle(itemTitle)
                .itemPrice(itemPrice)
                .categoryDTO(categoryDTO)
                .build();

        // Then
        assertEquals(itemsDTO.getItemId(), itemId);
        assertEquals(itemsDTO.getImgUrl(), imgUrl);
        assertEquals(itemsDTO.getItemTitle(), itemTitle);
        assertEquals(itemsDTO.getItemPrice(), itemPrice);
        assertEquals(itemsDTO.getCategoryDTO().getCategoryId(), categoryId);
        assertEquals(itemsDTO.getCategoryDTO().getCategoryTitle(), categoryTitle);
    }

    @DisplayName("ItemsDTO를 Items객체로 변경할 수 있다.")
    @Test
    void toEntity(){
        // Given
        Long itemId             = 10L;
        String imgUrl           = "IMG_URL";
        String itemTitle        = "아이템이름";
        Long itemPrice          = 1000L;
        Long categoryId         = 11L;
        String categoryTitle    = "카테고리명";
        CategoryDTO categoryDTO = CategoryDTO
                                            .builder()
                                            .categoryId(categoryId)
                                            .categoryTitle(categoryTitle)
                                            .build();
        ItemsDTO itemsDTO       = ItemsDTO.builder()
                                            .itemId(itemId)
                                            .imgUrl(imgUrl)
                                            .itemTitle(itemTitle)
                                            .itemPrice(itemPrice)
                                            .categoryDTO(categoryDTO)
                                            .build();

        // When
        Items items = ItemsDTO.toEntity(itemsDTO);

        // Then
        assertEquals(items.getItemId(), itemId);
        assertEquals(items.getImgUrl(), imgUrl);
        assertEquals(items.getItemTitle(), itemTitle);
        assertEquals(items.getItemPrice(), itemPrice);
        assertEquals(items.getCategory().getCategoryId(), categoryId);
        assertEquals(items.getCategory().getCategoryTitle(), categoryTitle);
    }
}