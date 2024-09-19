package com.practice_back.dto;

import com.practice_back.entity.Authority;
import com.practice_back.entity.Cart;
import com.practice_back.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberDTOTest {
    @Autowired
    PasswordEncoder passwordEncoder;
    @DisplayName("빌더를 사용해 MemberDTO를 생성할 수 있다.")
    @Test
    void testMemberDTO(){
        // Given
        String email        = "kkk@gmail.com";
        String password     = "123";
        String phoneNumber  = "01055554444";
        Authority authority = Authority.ROLE_ADMIN;
        Long id = 11L;
        Cart cart = Cart.builder().id(id).build();

        // When
        MemberDTO memberDTO = MemberDTO.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .authority(authority)
                .cart(cart)
                .build();

        // Then
        assertEquals(memberDTO.getEmail(), email);
        assertEquals(memberDTO.getPassword(), password);
        assertEquals(memberDTO.getPhoneNumber(), phoneNumber);
        assertEquals(memberDTO.getAuthority(), authority);
        assertEquals(memberDTO.getCart().getId(), id);
    }
    @DisplayName("MemberDTO를 Member 객체로 변경할 수 있다.")
    @Test
    void toEntity(){
        // Given
        String email        = "kkk@gmail.com";
        String password     = "123";
        String phoneNumber  = "01055554444";
        Authority authority = Authority.ROLE_ADMIN;
        Long id = 11L;
        Cart cart = Cart.builder().id(id).build();
        MemberDTO memberDTO = MemberDTO.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .authority(authority)
                .cart(cart)
                .build();

        // When
        Member member = MemberDTO.toEntity(memberDTO);

        // Then
        assertEquals(member.getEmail(), email);
        assertEquals(member.getPassword(), password);
        assertEquals(member.getPhoneNumber(), phoneNumber);
        assertEquals(member.getAuthority(), authority);
        assertEquals(member.getCart().getId(), id);
    }

    @DisplayName("이메일과 비밀번호 입력시 Member객체를 반환 받을 수 있다.")
    @Test
    void toMemberSignUp(){
        // Given
        String email        = "kkk@gmail.com";
        String unencPwd     = "123";
        Authority authority = Authority.ROLE_ADMIN;
        MemberDTO memberDTO = MemberDTO.builder()
                .email(email)
                .password(unencPwd)
                .build();

        // When
        Member member = memberDTO.toMemberSignUp(authority, passwordEncoder);

        // Then
        assertEquals(member.getEmail(), email);
        assertEquals(member.getId(), email);
        assertTrue(passwordEncoder.matches(unencPwd, member.getPassword()));
        assertEquals(member.getAuthority(), authority);
    }

}