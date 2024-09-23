package com.practice_back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class CartRequestDTOTest {
    @DisplayName("빌더를 통해 CartRequestDTO 객체를 생성할 수 있다.")
    @Test
    void createCartRequestDTO(){
        // Given
        Integer quantity    = 99;
        Long    itemId      = 9L;

        // When
        CartRequestDTO cartRequestDTO = CartRequestDTO.builder()
                .quantity(quantity)
                .itemId(itemId)
                .build();

        // Then
        assertEquals(cartRequestDTO.getQuantity(), quantity);
        assertEquals(cartRequestDTO.getItemId(), itemId);
    }
}