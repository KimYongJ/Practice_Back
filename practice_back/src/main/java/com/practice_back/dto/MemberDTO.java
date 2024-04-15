package com.practice_back.dto;

import com.practice_back.entity.Authority;
import com.practice_back.entity.Cart;
import com.practice_back.entity.DeliveryAddress;
import com.practice_back.entity.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String      email;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+])(?=\\S+$).{8,20}", message = "비밀번호는 8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String      password;
    @Pattern(regexp = "^01(?:0-?([0-9]{4})-?([0-9]{4})|1-?([0-9]{3})-?([0-9]{4})|9-?([0-9]{3})-?([0-9]{4}))$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
    private String      phoneNumber;
    private Authority   authority;
    private Cart        cart;

    public static Member toEntity(MemberDTO memberDto){
        return Member.builder()
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .phoneNumber(memberDto.getPhoneNumber())
                .authority(memberDto.getAuthority())
                .cart(memberDto.getCart())
                .build();
    }

    public Member toMemberSignUp(Authority authority,PasswordEncoder passwordEncoder) {
        return Member.builder()
                .id(email)
                .email(email)
                .password(passwordEncoder.encode(password))
                .authority(authority)
                .cart(new Cart())
                .build();
    }
}
