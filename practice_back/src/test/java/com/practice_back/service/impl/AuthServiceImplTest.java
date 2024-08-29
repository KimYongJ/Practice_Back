package com.practice_back.service.impl;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.dto.LoginDTO;
import com.practice_back.dto.MemberDTO;
import com.practice_back.entity.Authority;
import com.practice_back.entity.Member;
import com.practice_back.jwt.TokenProvider;
import com.practice_back.repository.CartRepository;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceImplTest {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartRepository cartRepository;
    @SpyBean
    AuthServiceImpl authServiceImpl;
    @AfterEach
    void tearDown(){
        cartRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("가입자 정보와, 응답을 보내면 회원 가입이된다.")
    @Test
    void signup(){
        // Given
        String email = "korean@naver.com";
        String pwd = "123";
        MemberDTO memDTO = createMemberDTO(email, pwd);
        MockHttpServletResponse response = new MockHttpServletResponse();
        // When
        MemberDTO result = authServiceImpl.signup(memDTO,response);
        // Then
        assertEquals(result.getEmail(), email);
        assertEquals(result.getAuthority(), Authority.ROLE_USER);
        assertThat(response)
                .satisfies(res->{
                    for(Cookie cookie : res.getCookies()){
                        assertEquals(cookie.getName(),"accessToken");
                    }
                });
    }
    @DisplayName("응답과 로그인 정보로 임시 토큰을 발급 받을 수 있다.")
    @Test
    @WithMockCustomUser(username="kkk@gmail.com", password = "1")
    void gettemptoken() throws Exception {
        // Given
        doNothing().when(authServiceImpl).validate(any());
        String email = "kkk@gmail.com";
        String password = "1";
        MockHttpServletResponse response = new MockHttpServletResponse();
        LoginDTO loginDTO = createLoginDTO(email, password);
        // When
        ResponseEntity<Object> result = authServiceImpl.gettemptoken(response,loginDTO);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(HttpStatus.MOVED_PERMANENTLY, res.getStatusCode());
                    Message msg = (Message)res.getBody();
                    assertEquals(msg.getStatus(), ErrorType.MOVED_PERMANENTLY);
                    assertEquals(msg.getMessage(),"확인 완료.");
                    assertEquals((String)msg.getData(),"account");
                });
        assertThat(response)
                .satisfies(res->{
                    for(Cookie cookie : res.getCookies()){
                        assertEquals(cookie.getName(),"tempToken");
                    }
                });
    }
    @DisplayName("잘못된 아이디와 비밀번호 입력시 BadRequest를 출력한다.")
    @Test
    void shouldReturnBadRequestWhenLoginCredentialsAreInvalid(){
        // Given
        String email = "kkk@gmail.com";
        String password = "1";
        MockHttpServletResponse response = new MockHttpServletResponse();
        LoginDTO loginDTO = createLoginDTO(email, password);
        // When
        ResponseEntity<Object> result = authServiceImpl.gettemptoken(response,loginDTO);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
                    Message msg = (Message) res.getBody();
                    assertEquals(msg.getStatus(),ErrorType.INVALID_LOGIN_CREDENTIALS);
                    assertEquals(msg.getMessage(),ErrorType.INVALID_LOGIN_CREDENTIALS.getErrStr());
                    assertNull(msg.getData());
                });
    }
    @DisplayName("요청에 tempToken을 검증하며 유효할 경우 true및 ErrorType.VALID_TOKEN 반환")
    @Test
    void validateTmpToken(){
        // Given
        String email = "kkk@naver.com";
        String tokenName = "tempToken";
        String tokenValue = tokenProvider.createTempToken(email, Authority.ROLE_USER.name(), new Date());
        Cookie cookie = new Cookie(tokenName, tokenValue);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(cookie);
        // When
        ResponseEntity<Object> result = authServiceImpl.validateTmpToken(request);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(),HttpStatus.OK);
                    Message msg = (Message)res.getBody();
                    assertEquals(msg.getStatus(), ErrorType.VALID_TOKEN);
                    assertEquals(msg.getMessage(), ErrorType.VALID_TOKEN.toString());
                    assertTrue((boolean)msg.getData());
                });
    }
    @DisplayName("tempToken이 없거나 만료일이 자났으면 false 및 ErrorType.INVALID_TOKEN 반환")
    @Test
    void validateTmpToken_InvalidToken(){
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        // When
        ResponseEntity<Object> result = authServiceImpl.validateTmpToken(request);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(),HttpStatus.OK);
                    Message msg = (Message)res.getBody();
                    assertEquals(msg.getStatus(), ErrorType.INVALID_TOKEN);
                    assertEquals(msg.getMessage(), ErrorType.INVALID_TOKEN.toString());
                    assertFalse((boolean)msg.getData());
                });
    }
    @DisplayName("리캡챠 테스트")
    @Test
    void verifyRecaptcha(){
        // Given
        Map<String, Object> recaptchaData = new HashMap<>(){{put("token","temp");}};
        Map<String,Object> response = new HashMap<>(){{put("success", true);}};
        doReturn(response).when(authServiceImpl).postForObject(any(), any());
        // When
        ResponseEntity<Object> result = authServiceImpl.verifyRecaptcha(recaptchaData);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(), HttpStatus.OK);
                    Message msg = (Message)res.getBody();
                    assertEquals(msg.getMessage(),"수신 완료");
                    assertEquals(msg.getStatus(), ErrorType.AUTHENTICATION_SUCCESS);
                    assertTrue((boolean)msg.getData());
                });
    }
    @DisplayName("이메일을 통해 멤버 유무를 확인할 수 있다.")
    @Test
    void existsByEmail(){
        // Given
        String email = "dummy1@naver.com";
        String password = "123";
        Member member1 = createMember(email,password);
        memberRepository.save(member1);
        // When
        boolean bool1 = authServiceImpl.existsByEmail(email);
        boolean bool2 = authServiceImpl.existsByEmail("-");
        // Then
        assertTrue(bool1);
        assertFalse(bool2);
    }
    @DisplayName("아이디를 통해 멤버를 가져올 수 있다")
    @Test
    void findById(){
        // Given
        String email = "dummy1@naver.com";
        String password = "123";
        Member member1 = createMember(email,password);
        memberRepository.save(member1);
        // When
        Optional<Member> member = authServiceImpl.findById(email);
        // Then
        assertThat(member)
                .isPresent() // optional이 비어있지 않은지 확인
                .get()// Optional에서 실제 member 객체 추출
                .extracting("email","password")
                .containsExactly(email, password);
    }
    public LoginDTO createLoginDTO(String email, String password){
        return LoginDTO.builder()
                .email(email)
                .password(password)
                .build();
    }
    public MemberDTO createMemberDTO(String email, String pwd){
        return MemberDTO.builder()
                .email(email)
                .password(pwd)
                .build();
    }
    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .password(pwd)
                .build();
    }
}