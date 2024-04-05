package com.practice_back.dto;


import com.practice_back.entity.Cart;
import com.practice_back.entity.CartItem;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private List<CartItemDTO> cartItemsDTO; // 카트에 담긴 아이탬들의 종류
}
