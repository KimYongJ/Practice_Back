package com.practice_back.entity;

import com.practice_back.dto.CategoryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class CategoryTest {
    @DisplayName("카테고리 객체를 빌더를 통해 생성할 수 있다.")
    @Test
    void createCategoryEntity(){
        // Given
        Long categoryId         = 10L;
        String categoryTitle    = "카테고리명";
        // When
        Category category = Category.builder()
                .categoryId(categoryId)
                .categoryTitle(categoryTitle)
                .build();
        // Then
        assertEquals(category.getCategoryId(),      categoryId);
        assertEquals(category.getCategoryTitle(),   categoryTitle);
    }
    @DisplayName("엔티티를 DTO로 변경할 수 있다.")
    @Test
    void toDTO(){
        // Given
        Long categoryId         = 10L;
        String categoryTitle    = "카테고리명";
        Category category = Category.builder()
                .categoryId(categoryId)
                .categoryTitle(categoryTitle)
                .build();
        // When
        CategoryDTO categoryDTO = Category.toDTO(category);
        // Then
        assertEquals(categoryDTO.getCategoryId(),       categoryId);
        assertEquals(categoryDTO.getCategoryTitle(),    categoryTitle);
    }
}