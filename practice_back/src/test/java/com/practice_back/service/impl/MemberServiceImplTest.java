package com.practice_back.service.impl;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.dto.PasswordDTO;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceImplTest {
    @Autowired
    PasswordEncoder passwordEncoder;
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
    void getUserProfile(){
        // Given
        Member member = createMember("kyj","1");
        memberRepository.save(member);
        // When
        ResponseEntity<Object> result = memberServiceImpl.getUserProfile();
        // Then
        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);
                            Message msg = (Message)result.getBody();
                            assertEquals(HttpStatus.OK,result.getStatusCode());
                            assertEquals(ErrorType.OK, msg.getStatus());
                            assertThat(msg.getMessage()).isEqualTo("조회 완료");
                            assertThat((UserProfileDTO) msg.getData())
                                    .satisfies(dto-> {
                                        assertThat(dto.getEmail()).isEqualTo("kyj");
                                        assertThat(dto.getPhoneNumber()).isEqualTo("123-456-7890");
                                    });
                        });

    }

    @DisplayName("휴대폰 번호를 업데이트할 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password="1")
    void updateProfile(){
        // Given
        Member member = createMember("kyj","1");
        memberRepository.save(member);
        UserProfileDTO userProfileDTO = createMUserProfileDTO("010-1234-5678");
        // When
        Optional<Member> member1 = memberRepository.findById("kyj");
        ResponseEntity<Object> result = memberServiceImpl.updateProfile(userProfileDTO);

        // Then
        assertThat(member1)
                .isPresent()
                .get()
                .satisfies(mem->{
                    assertThat(mem.getPhoneNumber()).isEqualTo("010-1234-5678");
                });

        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);
                            Message msg = (Message)result.getBody();
                            assertEquals(ErrorType.OK, msg.getStatus());
                            assertThat(msg.getMessage()).isEqualTo("변경되었습니다.");
                            assertThat(msg.getData()).isEqualTo("010-1234-5678");
                        });
    }
    @DisplayName("비밀번호를 변경할 수 있다")
    @Test
    @WithMockCustomUser(username="kyj", password="1")
    void updatePassword(){
        // Given
        Member member = createMember("kyj","1");
        memberRepository.save(member);
        PasswordDTO passwordDTO1 = createPasswordDTO("1","2");
        PasswordDTO passwordDTO2 = createPasswordDTO("1","1");
        PasswordDTO passwordDTO3 = createPasswordDTO("2","2");
        // When
        ResponseEntity<Object> result1 = memberServiceImpl.updatePassword(passwordDTO1);
        ResponseEntity<Object> result2 = memberServiceImpl.updatePassword(passwordDTO2);
        ResponseEntity<Object> result3 = memberServiceImpl.updatePassword(passwordDTO3);
        // Then
        assertThat(result1).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.BAD_REQUEST);
                            Message msg = (Message)res.getBody();
                            assertThat(msg)
                                    .extracting("status","message","data")
                                    .containsExactly(ErrorType.INVALID_PASSWORD,"유효하지 않은 비밀번호입니다.", null);
                        });
        assertThat(result2).isNotNull()
                .satisfies(res->{
                    assertEquals(res.getStatusCode(),HttpStatus.BAD_REQUEST);
                    Message msg = (Message)res.getBody();
                    assertThat(msg)
                            .extracting("status","message","data")
                            .containsExactly(ErrorType.PASSWORD_NO_CHANGE,"기존 비밀번호와 같습니다. 다른 비밀번호를 사용해주세요.", null);
                });
        assertThat(result3).isNotNull()
                .satisfies(res->{
                    assertEquals(res.getStatusCode(),HttpStatus.OK);
                    Message msg = (Message)res.getBody();
                    assertThat(msg)
                            .extracting("status","message","data")
                            .containsExactly(ErrorType.OK,"변경되었습니다.", null);
                });
    }
    @DisplayName("요청만으로 탈퇴할 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password="1")
    void deleteById(){
        // Given
        HttpServletResponse response = new MockHttpServletResponse();
        Member member = createMember("kyj","1");
        memberRepository.save(member);
        // When
        ResponseEntity<Object> result1 = memberServiceImpl.deleteById(response);
        ResponseEntity<Object> result2 = memberServiceImpl.deleteById(response);
        // Then
        assertThat(result1).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);
                            Message msg = (Message)res.getBody();
                            assertThat(msg)
                                    .extracting("status","message","data")
                                    .containsExactly(ErrorType.ACCOUNT_DELETION_SUCCESS,ErrorType.ACCOUNT_DELETION_SUCCESS.getErrStr(),1);
                        });
        assertThat(result2).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);
                            Message msg = (Message)res.getBody();
                            assertThat(msg)
                                    .extracting("status","message","data")
                                    .containsExactly(ErrorType.BAD_REQUEST,ErrorType.BAD_REQUEST.getErrStr(),0);
                        });
    }
    public PasswordDTO createPasswordDTO(String newPassword,String newPasswordConfirm){
        return PasswordDTO.builder()
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .build();
    }
    public UserProfileDTO createMUserProfileDTO(String phone){
        return UserProfileDTO.builder()
                .phoneNumber(phone)
                .build();
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