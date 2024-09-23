package com.practice_back.dto;
import lombok.*;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {
    @NotNull(message = "수량 정보가 누락되었습니다.")
    private Integer quantity;
    @NotNull(message = "상품 정보가 누락되었습니다.")
    private Long    itemId;
}