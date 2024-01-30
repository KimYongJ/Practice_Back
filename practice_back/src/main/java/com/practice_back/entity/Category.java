package com.practice_back.entity;

import com.practice_back.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter                                     // 갯터
@Entity                                     // 엔티티임을 알리는 어노테이션
@Builder                                    // 빌더
@AllArgsConstructor                         // 모든 필드 값을 파라미터로 받는 생성자를 만들어줌
@NoArgsConstructor                          // 파라미터가 없는 기본 생성자 생성
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;                // 카테고리 고유 번호

    @Column(name = "category_title",nullable=false)
    private String categoryTitle;           // 카테고리 제목( ex 국내차용품 ~ DIY용품 )


    public static CategoryDTO toDTO(Category category)
    {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle())
                .build();
    }
}
