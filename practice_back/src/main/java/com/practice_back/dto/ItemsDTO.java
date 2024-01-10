package com.practice_back.dto;
import com.practice_back.entity.Items;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDTO {

    private Long itemId;            // 아이템의 고유 번호

    @Size(max = 10,message = "카테고리 값은 10이하로 입력해주세요.")
    @NotBlank(message = "카테고리 값을 입력해주세요.")
    private String category;    // 아이템 카테고리( ex 0=국내차용품 ~ 6=DIY용품 )

    private String imgUrl;          // 이미지 주소

    @NotBlank(message = "상품명을 입력해주세요.")
    private String itemTitle;       // 아이템 이름
    @NotBlank(message = "상품 가격을 입력해주세요.")
    private Long itemPrice;          // 아이템 가격

    public static Items toItemsEntity(ItemsDTO itemsDTO){
        return Items.builder()
                .itemId(itemsDTO.getItemId())
                .category(itemsDTO.getCategory())
                .imgUrl(itemsDTO.getImgUrl())
                .itemTitle(itemsDTO.getItemTitle())
                .itemPrice(itemsDTO.getItemPrice())
                .build();
    }
}
