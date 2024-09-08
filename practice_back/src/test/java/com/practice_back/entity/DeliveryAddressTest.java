package com.practice_back.entity;

import com.practice_back.dto.DeliveryAddressDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryAddressTest {
    @DisplayName("")
    @Test
    void createDeliveryAddressEntity(){
        // Given
        Long deliveryAddressId  = 10L;
        String recipient        = "가나다";      // 수령인
        String contactNumber    = "01012345678";// 수령인 전화번호
        String postalCode       = "5555";       // 우편번호
        String address          = "서울시 도봉구";// 배송지 전체 주소
        String addressDetail    = "마들로 656";  // 상세 주소
        Boolean isPrimary       = true;         // 기본 배송지 여부
        // When
        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .deliveryAddressId(deliveryAddressId)
                .recipient(recipient)
                .contactNumber(contactNumber)
                .postalCode(postalCode)
                .address(address)
                .addressDetail(addressDetail)
                .isPrimary(isPrimary)
                .build();
        // Then
        assertEquals(deliveryAddress.getDeliveryAddressId(),    deliveryAddressId);
        assertEquals(deliveryAddress.getRecipient(),            recipient);
        assertEquals(deliveryAddress.getContactNumber(),        contactNumber);
        assertEquals(deliveryAddress.getPostalCode(),           postalCode);
        assertEquals(deliveryAddress.getAddress(),              address);
        assertEquals(deliveryAddress.getAddressDetail(),        addressDetail);
        assertEquals(deliveryAddress.getIsPrimary(),            isPrimary);
    }
    @DisplayName("기본 배송지 여부를 함수를 통해 바꿀 수 있다.")
    @Test
    void changePrimary(){
        // Given
        Boolean isPrimary = true;// 기본 배송지 여부
        // When
        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .isPrimary(isPrimary)
                .build();
        // When Then
        deliveryAddress.makePrimary();
        assertTrue(deliveryAddress.getIsPrimary());
        deliveryAddress.makeNotPrimary();
        assertFalse(deliveryAddress.getIsPrimary());
    }
    @DisplayName("DeliveryAddressDTO 객체의 값으로 현재 엔티티를 업데이트할 수 있다.")
    @Test
    void entityUpdate(){
        // Given
        String recipient        = "가나다";      // 수령인
        String contactNumber    = "01012345678";// 수령인 전화번호
        String postalCode       = "5555";       // 우편번호
        String address          = "서울시 도봉구";// 배송지 전체 주소
        String addressDetail    = "마들로 656";  // 상세 주소
        Boolean isPrimary       = true;         // 기본 배송지 여부

        DeliveryAddressDTO DTO = DeliveryAddressDTO.builder()
                .recipient(recipient)
                .contactNumber(contactNumber)
                .postalCode(postalCode)
                .address(address)
                .addressDetail(addressDetail)
                .isPrimary(isPrimary)
                .build();

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        // When
        deliveryAddress.update(DTO);
        // Then
        assertEquals(deliveryAddress.getRecipient(),            recipient);
        assertEquals(deliveryAddress.getContactNumber(),        contactNumber);
        assertEquals(deliveryAddress.getPostalCode(),           postalCode);
        assertEquals(deliveryAddress.getAddress(),              address);
        assertEquals(deliveryAddress.getAddressDetail(),        addressDetail);
        assertEquals(deliveryAddress.getIsPrimary(),            isPrimary);
    }
    @DisplayName("DeliveryAddress 객체를 DeliveryAddressDTO 객체로 변경할 수 있다.")
    @Test
    void toDTO(){
        // Given
        Long deliveryAddressId  = 10L;
        String recipient        = "가나다";      // 수령인
        String contactNumber    = "01012345678";// 수령인 전화번호
        String postalCode       = "5555";       // 우편번호
        String address          = "서울시 도봉구";// 배송지 전체 주소
        String addressDetail    = "마들로 656";  // 상세 주소
        Boolean isPrimary       = true;         // 기본 배송지 여부
        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .deliveryAddressId(deliveryAddressId)
                .recipient(recipient)
                .contactNumber(contactNumber)
                .postalCode(postalCode)
                .address(address)
                .addressDetail(addressDetail)
                .isPrimary(isPrimary)
                .build();
        // When
        DeliveryAddressDTO deliveryAddressDTO = DeliveryAddress.toDTO(deliveryAddress);
        // Then
        assertEquals(deliveryAddressDTO.getDeliveryAddressId(),    deliveryAddressId);
        assertEquals(deliveryAddressDTO.getRecipient(),            recipient);
        assertEquals(deliveryAddressDTO.getContactNumber(),        contactNumber);
        assertEquals(deliveryAddressDTO.getPostalCode(),           postalCode);
        assertEquals(deliveryAddressDTO.getAddress(),              address);
        assertEquals(deliveryAddressDTO.getAddressDetail(),        addressDetail);
        assertEquals(deliveryAddressDTO.getIsPrimary(),            isPrimary);
    }
}