package com.practice_back.dto;
import com.practice_back.entity.Items;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDTO {

    private Long itemId;            // 아이템의 고유 번호
    private String category;    // 아이템 카테고리( ex 0=국내차용품 ~ 6=DIY용품 )
    private String imgUrl;          // 이미지 주소
    private String itemTitle;       // 아이템 이름
    private int itemPrice;          // 아이템 가격

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
