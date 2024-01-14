package com.practice_back.entity;

import com.practice_back.dto.ItemsDTO;
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
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;                    // 아이템의 고유 번호

    @Column(name = "category",nullable=false)
    private String category;            // 아이템 카테고리( ex 국내차용품 ~ DIY용품 )

    @Column(name = "img_url",nullable=false)
    private String imgUrl;                  // 이미지 주소

    @Column(name = "item_title",nullable = false)
    private String itemTitle;               // 아이템 이름

    @Column(name = "item_price",nullable = false)
    private Long itemPrice;                  // 아이템 가격

    public static ItemsDTO toItemsDTO(Items items){
        return ItemsDTO.builder()
                .itemId(items.getItemId())
                .category(items.getCategory())
                .imgUrl(items.getImgUrl())
                .itemTitle(items.getItemTitle())
                .itemPrice(items.getItemPrice())
                .build();
    }
}
