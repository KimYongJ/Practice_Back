package com.practice_back.dto;

import com.practice_back.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class CategoryDTOTest {
    @DisplayName("빌더를 통해 CategoryDTO 객체를 생성할 수 있다.")
    @Test
    void createCategoryDTO(){
        // Given
        Long categoryId = 10L;
        String categoryTitle = "카테고리명";

        // When
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryId(categoryId)
                .categoryTitle(categoryTitle)
                .build();

        // Then
        assertEquals(categoryDTO.getCategoryId(), categoryId);
        assertEquals(categoryDTO.getCategoryTitle(), categoryTitle);
    }
    @DisplayName("CategoryDTO 객체를 Category 엔티티로 변경할 수 있다.")
    @Test
    void toEntity(){
        // Given
        Long categoryId = 10L;
        String categoryTitle = "카테고리명";
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryId(categoryId)
                .categoryTitle(categoryTitle)
                .build();

        // When
        Category category = CategoryDTO.toEntity(categoryDTO);

        // Then
        assertEquals(category.getCategoryId(), categoryId);
        assertEquals(category.getCategoryTitle(), categoryTitle);
    }
}