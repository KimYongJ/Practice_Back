package com.practice_back.dto;

import com.practice_back.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data// Getter , Setter, toString, equals, hashCode 자동생성
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long categoryId;                // 카테고리 고유 번호
    private String categoryTitle;           // 카테고리 제목( ex 국내차용품 ~ DIY용품 )

    public static Category toCategoryEntity(CategoryDTO categoryDTO)
    {
        return Category.builder()
                .categoryId(categoryDTO.getCategoryId())
                .categoryTitle(categoryDTO.getCategoryTitle())
                .build();
    }

}
