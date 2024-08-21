package com.practice_back.service.impl;

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
class EmailAuthServiceImplTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EmailAuthServiceImpl emailAuthService;
    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }
    @DisplayName("없는 아이디 전달시 에러를 출력한다")
    @Test
    void testSendNewPasswordWithNonexistentIdThrowsError(){
        // Given
        String ID = "Empty";
        // When
        ResponseEntity<Object> result = emailAuthService.sendNewPassword(ID);
        // Then
        assertThat(result).isNotNull()
                .satisfies(res->{
                    assertEquals(HttpStatus.BAD_REQUEST,res.getStatusCode());

                    Message msg = (Message)result.getBody();

                    assertEquals(ErrorType.EMAIL_NOT_FOUND,msg.getStatus());
                    assertEquals("해당하는 이메일이 없습니다.", msg.getMessage());
                    assertEquals(ID, msg.getData());
                });
    }
    @DisplayName("아이디 전달시 임시비밀번호가 메일로 발송된다.")
    @Test
    void sendNewPassword(){
        // Given
        String email = "yongj326@naver.com";
        String pwd = "111";
        Member member = createMember(email,pwd);
        memberRepository.save(member);
        // When
        ResponseEntity<Object> result = emailAuthService.sendNewPassword(email);
        // Then
        assertThat(result).isNotNull()
                .satisfies(res->{
                    assertEquals(HttpStatus.OK,res.getStatusCode());

                    Message msg = (Message)result.getBody();

                    assertEquals(ErrorType.OK,msg.getStatus());
                    assertEquals("이메일을 확인하세요!", msg.getMessage());
                    assertEquals(null, msg.getData());
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