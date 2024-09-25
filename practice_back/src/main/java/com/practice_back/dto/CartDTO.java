package com.practice_back.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private List<CartItemDTO> cartItemsDTO; // 카트에 담긴 아이탬들의 종류
}
