package com.practice_back.dto;

import com.practice_back.entity.CartItem;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long id;
    private int quantity;           // 상품 수량
    private long totalPrice;        // 상품 수량에 따른 총금액
    private Long cartId;            // Cart의 ID
    private ItemsDTO itemsDTO;
}
