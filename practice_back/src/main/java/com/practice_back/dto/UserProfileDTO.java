package com.practice_back.dto;

import lombok.*;

import javax.validation.constraints.Pattern;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String  email;
    @Pattern(regexp = "^01(?:0-?([0-9]{4})-?([0-9]{4})|1-?([0-9]{3})-?([0-9]{4})|9-?([0-9]{3})-?([0-9]{4}))$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
    private String  phoneNumber;
    private String picture;
    private boolean master;
    private DeliveryAddressDTO deliveryAddressDTO;
}
