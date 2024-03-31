package com.practice_back.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+])(?=\\S+$).{8,20}", message = "비밀번호는 8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String newPassword;
    private String newPasswordConfirm;
}
