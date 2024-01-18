package com.practice_back.dto;
import com.practice_back.entity.Category;
import com.practice_back.entity.Items;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data// Getter , Setter, toString, equals, hashCode 자동생성
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDTO {

    private Long itemId;            // 아이템의 고유 번호

    private String imgUrl;          // 이미지 주소

    @NotBlank(message = "상품명을 입력해주세요.")
    private String itemTitle;       // 아이템 이름

    @NotBlank(message = "상품 가격을 입력해주세요.")
    private Long itemPrice;         // 아이템 가격

    private CategoryDTO categoryDTO;// 아이템 카테고리( ex 국내차용품 ~ DIY용품 )

    public static Items toItemsEntity(ItemsDTO itemsDTO){
        return Items.builder()
                .itemId(itemsDTO.getItemId())
                .category(CategoryDTO.toCategoryEntity(itemsDTO.getCategoryDTO()))
                .imgUrl(itemsDTO.getImgUrl())
                .itemTitle(itemsDTO.getItemTitle())
                .itemPrice(itemsDTO.getItemPrice())
                .build();
    }
}
