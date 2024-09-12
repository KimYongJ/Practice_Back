package com.practice_back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserProfileDTOTest {
    @DisplayName("빌더를 통해 UserProfileDTO 객체를 생성할 수 있다.")
    @Test
    void testUserProfileDTO(){
        // Given
        String  email       = "kkk@gmail.com";
        String  phoneNumber = "010-8888-7777";
        String picture      = "IMG";
        boolean master      = true;
        String providerId   = "NAVER";
        String address      = "임시주소";
        DeliveryAddressDTO deliveryAddressDTO = DeliveryAddressDTO.builder()
                .address(address)
                .build();

        // When
        UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .picture(picture)
                .master(master)
                .providerId(providerId)
                .deliveryAddressDTO(deliveryAddressDTO)
                .build();

        // Then
        assertEquals(userProfileDTO.getEmail(), email);
        assertEquals(userProfileDTO.getPhoneNumber(), phoneNumber);
        assertEquals(userProfileDTO.getPicture(), picture);
        assertEquals(userProfileDTO.isMaster(), master);
        assertEquals(userProfileDTO.getProviderId(), providerId);
        assertEquals(userProfileDTO.getDeliveryAddressDTO().getAddress(), address);
    }
}