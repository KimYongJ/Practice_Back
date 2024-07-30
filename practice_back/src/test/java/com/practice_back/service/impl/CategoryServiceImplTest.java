package com.practice_back.service.impl;

import com.practice_back.dto.CategoryDTO;
import com.practice_back.entity.Category;
import com.practice_back.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceImplTest {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryServiceImpl categoryServiceImpl;
    @AfterEach
    void tearDown(){
        categoryRepository.deleteAllInBatch();
    }
    @DisplayName("모든 카테고리를 조회할 수 있다.")
    @Test
    void getCategories(){
        // Given
        Category category1 = createCategory("카테고리1");
        Category category2 = createCategory("카테고리2");
        Category category3 = createCategory("카테고리3");
        categoryRepository.saveAll(List.of(category1,category2,category3));
        // When
        List<CategoryDTO> result = categoryServiceImpl.getCategories();
        // Then
        assertThat(result)
                .hasSize(3)
                .extracting("categoryTitle")
                .containsExactlyInAnyOrder("카테고리1", "카테고리2", "카테고리3");
    }

    @DisplayName("카테고리 ID로 조회할 수 있다.")
    @Test
    void test(){
        // Given
        Category category1 = createCategory("카테고리1");
        Category category2 = createCategory("카테고리2");
        Category category3 = createCategory("카테고리3");
        categoryRepository.saveAll(List.of(category1,category2,category3));
        // When
        Optional<Category> result1 = categoryServiceImpl.findById(1L);
        Optional<Category> result2 = categoryServiceImpl.findById(2L);
        Optional<Category> result3 = categoryServiceImpl.findById(3L);
        // Then
        assertThat(result1).isPresent().get().extracting("categoryTitle").isEqualTo("카테고리1");
        assertThat(result2).isPresent().get().extracting("categoryTitle").isEqualTo("카테고리2");
        assertThat(result3).isPresent().get().extracting("categoryTitle").isEqualTo("카테고리3");
    }
    public Category createCategory(String title){
        return Category.builder()
                .categoryTitle(title)
                .build();
    }
}