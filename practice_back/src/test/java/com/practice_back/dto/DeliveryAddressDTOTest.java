package com.practice_back.dto;
import com.practice_back.entity.DeliveryAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class DeliveryAddressDTOTest {
    @DisplayName("빌더를 통해 DeliveryAddressDTO객체를 생성할 수 있다.")
    @Test
    void createDeliveryAddressDTO(){
        // Given
        Long deliveryAddressId = 10L;
        String recipient = "수령인";
        String contactNumber = "01055554444";
        String postalCode = "55555";
        String address = "경기도 성남시";
        String addressDetail = "분당구 정자일로 93";
        Boolean isPrimary = true;

        // When
        DeliveryAddressDTO deliveryAddressDTO = DeliveryAddressDTO.builder()
                                                    .deliveryAddressId(deliveryAddressId)
                                                    .recipient(recipient)
                                                    .contactNumber(contactNumber)
                                                    .postalCode(postalCode)
                                                    .address(address)
                                                    .addressDetail(addressDetail)
                                                    .isPrimary(isPrimary)
                                                    .build();

        // Then
        assertEquals(deliveryAddressDTO.getDeliveryAddressId(), deliveryAddressId);
        assertEquals(deliveryAddressDTO.getRecipient(), recipient);
        assertEquals(deliveryAddressDTO.getContactNumber(), contactNumber);
        assertEquals(deliveryAddressDTO.getPostalCode(), postalCode);
        assertEquals(deliveryAddressDTO.getAddress(), address);
        assertEquals(deliveryAddressDTO.getAddressDetail(), addressDetail);
        assertEquals(deliveryAddressDTO.getIsPrimary(), isPrimary);
    }

    @DisplayName("DeliveryAddressDTO 객체를 DeliveryAddress 엔티티로 변경할 수 있다.")
    @Test
    void toEntity(){
        // Given
        Long deliveryAddressId = 10L;
        String recipient = "수령인";
        String contactNumber = "01055554444";
        String postalCode = "55555";
        String address = "경기도 성남시";
        String addressDetail = "분당구 정자일로 93";
        Boolean isPrimary = true;

        DeliveryAddressDTO deliveryAddressDTO = DeliveryAddressDTO.builder()
                .deliveryAddressId(deliveryAddressId)
                .recipient(recipient)
                .contactNumber(contactNumber)
                .postalCode(postalCode)
                .address(address)
                .addressDetail(addressDetail)
                .isPrimary(isPrimary)
                .build();
        // When
        DeliveryAddress deliveryAddress = DeliveryAddressDTO.toEntity(deliveryAddressDTO);

        // Then
        assertEquals(deliveryAddress.getDeliveryAddressId(), deliveryAddressId);
        assertEquals(deliveryAddress.getRecipient(), recipient);
        assertEquals(deliveryAddress.getContactNumber(), contactNumber);
        assertEquals(deliveryAddress.getPostalCode(), postalCode);
        assertEquals(deliveryAddress.getAddress(), address);
        assertEquals(deliveryAddress.getAddressDetail(), addressDetail);
        assertEquals(deliveryAddress.getIsPrimary(), isPrimary);
    }
}