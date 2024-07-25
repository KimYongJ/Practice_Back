package com.practice_back.service.impl;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.dto.UserProfileDTO;
import com.practice_back.entity.Authority;
import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceImplTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberServiceImpl memberServiceImpl;
    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }
    @DisplayName("회원 정보를 조회할 수 있다")
    @Test
    @WithMockCustomUser(username="kyj", password="1")
    void test(){
        // Given
        Member member = createMember("kyj","1");
        memberRepository.save(member);
        // When
        ResponseEntity<Object> result = memberServiceImpl.getUserProfile();
        Message resultMsg = (Message)result.getBody();
        UserProfileDTO resultDTO = (UserProfileDTO) resultMsg.getData();
        // Then
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(ErrorType.OK, resultMsg.getStatus());
        assertThat(resultMsg.getMessage()).isEqualTo("조회 완료");
        assertThat(resultDTO)
                .satisfies(dto-> {
                    assertThat(dto.getEmail()).isEqualTo("kyj");
                    assertThat(dto.getPhoneNumber()).isEqualTo("123-456-7890");
                });
    }

    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .password(pwd)
                .phoneNumber("123-456-7890")
                .authority(Authority.ROLE_USER)
                .deliveryAddresses(new ArrayList<>()) // 빈 리스트로 초기화
                .build();
    }
}