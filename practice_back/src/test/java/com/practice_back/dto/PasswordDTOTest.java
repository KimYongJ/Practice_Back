package com.practice_back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PasswordDTOTest {
    @DisplayName("빌더를 통해 PasswordDTO 객체를 생성할 수 있다.")
    @Test
    void testPasswordDTO(){
        // Given
        String newPassword = "123";
        String newPasswordConfirm = "123";

        // When
        PasswordDTO passwordDTO = PasswordDTO.builder()
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .build();

        // Then
        assertEquals(passwordDTO.getNewPassword(), newPassword);
        assertEquals(passwordDTO.getNewPasswordConfirm(), newPasswordConfirm);
    }
}