package com.practice_back.dto;

import com.practice_back.entity.Category;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long    categoryId;      // 카테고리 고유 번호
    private String  categoryTitle;   // 카테고리 제목( ex 국내차용품 ~ DIY용품 )

    public static Category toEntity(CategoryDTO categoryDTO)
    {
        return Category.builder()
                .categoryId(categoryDTO.getCategoryId())
                .categoryTitle(categoryDTO.getCategoryTitle())
                .build();
    }

}
