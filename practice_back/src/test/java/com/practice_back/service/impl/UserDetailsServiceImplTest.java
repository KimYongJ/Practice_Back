package com.practice_back.service.impl;

import com.practice_back.entity.Authority;
import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserDetailsServiceImplTest {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }
    @DisplayName("ID전달시 UserDeatils 객체를 받을 수 있다.")
    @Test
    void loadUserByUsername(){
        // Given
        String ID = "KYJ";
        Member member = createMember(ID,ID);
        memberRepository.save(member);

        // When
        UserDetails res = userDetailsService.loadUserByUsername(ID);
        UserDetails expected = new User(
                String.valueOf(member.getEmail()),
                member.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().name()))
        );
        // Then
        assertNotNull(res);
        assertEquals(expected.getUsername(),res.getUsername(), "Usernames should match");
        assertEquals(expected.getPassword(),res.getPassword(), "Passwords should match");
        assertEquals(expected.getAuthorities(),res.getAuthorities(), "Authorities should match");
    }
    @DisplayName("없는 ID전달시 UsernameNotFoundException 클래스 예외가 발생한다.")
    @Test
    void loadUserByUsernameThrowsExceptionForUnknownId(){
        // Given When Then
        String ID = "ISNULL";
        assertThatThrownBy(()->userDetailsService.loadUserByUsername(ID))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(ID + " 을 DB에서 찾을 수 없습니다");
    }
    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .password(passwordEncoder.encode(pwd))
                .phoneNumber("123-456-7890")
                .authority(Authority.ROLE_USER)
                .deliveryAddresses(new ArrayList<>()) // 빈 리스트로 초기화
                .build();
    }
}