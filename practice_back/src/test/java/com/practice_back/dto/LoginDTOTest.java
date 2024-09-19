package com.practice_back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LoginDTOTest {
    @DisplayName("빌더를 사용해 LoginDTO객체를 생성할 수 있다.")
    @Test
    void testLoginDTO(){
        // Given
        long cntCartItems   = 10L;
        boolean master      = true;
        String email        = "kkk@naver.com";
        String picture      = "IMG";
        String password     = "123";

        // When
        LoginDTO loginDTO = LoginDTO.builder()
                .cntCartItems(cntCartItems)
                .master(master)
                .email(email)
                .picture(picture)
                .password(password)
                .build();

        // Then
        assertEquals(loginDTO.getCntCartItems(), cntCartItems);
        assertTrue(loginDTO.isMaster());
        assertEquals(loginDTO.getEmail(), email);
        assertEquals(loginDTO.getPicture(), picture);
        assertEquals(loginDTO.getPassword(), password);
    }
}