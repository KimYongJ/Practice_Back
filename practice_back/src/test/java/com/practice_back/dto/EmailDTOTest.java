package com.practice_back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmailDTOTest {
    @DisplayName("EmailDTO객체를 생성하고 email을 변경, 조회 할 수 있다.")
    @Test
    void createEmailDTO(){
        // Given
        String email1 = "KKK@gmail.com";
        String email2 = "JJJ@gmail.com";
        // When Then
        EmailDTO emailDTO = new EmailDTO(email1);
        assertEquals(emailDTO.getEmail(), email1);

        emailDTO.setEmail(email2);
        assertEquals(emailDTO.getEmail(), email2);
    }
}