package com.practice_back.dto;

import com.practice_back.entity.DeliveryAddress;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddressDTO {


    private Long deliveryAddressId;
    @NotBlank(message = "수령인을 입력해주세요.")
    @Size(min = 1, max = 10, message = "수령인은 1자 이상 10자 미만이어야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "수령인은 한글과 영어만 포함할 수 있습니다.")
    private String recipient;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Size(min = 1, max = 12, message = "전화번호는 1자 이상 12자 미만이어야 합니다.")
    @Pattern(regexp = "^\\d+$", message = "전화번호는 숫자만 가능합니다.")
    private String contactNumber;

    @NotBlank(message = "우편번호를 입력해주세요.")
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자만 가능합니다.")
    private String postalCode;

    @NotBlank(message = "배송주소를 입력해주세요.")
    @Size(max = 50, message = "배송주소는 50자를 넘을 수 없습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "배송주소는 특수문자를 포함할 수 없으며, 한글, 영어, 숫자, 공백만 가능합니다.")
    private String address;

    @NotBlank(message = "상세 주소를 입력해주세요.")
    @Size(max = 50, message = "상세 주소는 50자를 넘을 수 없습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "상세 주소는 특수문자를 포함할 수 없으며, 한글, 영어, 숫자, 공백만 가능합니다.")
    private String addressDetail;


    private Boolean isPrimary = false;  // 기본 배송지 여부

    public static DeliveryAddress toEntity(DeliveryAddressDTO deliveryAddressDTO){
        return DeliveryAddress.builder()
                .deliveryAddressId(deliveryAddressDTO.getDeliveryAddressId())
                .recipient(deliveryAddressDTO.getRecipient())
                .contactNumber(deliveryAddressDTO.getContactNumber())
                .postalCode(deliveryAddressDTO.getPostalCode())
                .address(deliveryAddressDTO.getAddress())
                .addressDetail(deliveryAddressDTO.getAddressDetail())
                .isPrimary(deliveryAddressDTO.getIsPrimary())
                .build();
    }
}
