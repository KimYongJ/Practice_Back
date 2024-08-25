package com.practice_back.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice_back.entity.Authority;
import com.practice_back.entity.Member;
import com.practice_back.repository.CartItemRepository;
import com.practice_back.repository.CartRepository;
import com.practice_back.repository.DeliveryAddressRepository;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SpringBootTest
class CustomLoginFilterTest {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    private CustomLoginFilter customLoginFilter;
    @BeforeEach
    void setUp(){
        customLoginFilter = spy(new CustomLoginFilter(tokenProvider, cartRepository, memberRepository));
        customLoginFilter.setAuthenticationManager(authenticationManager);
    }
    @AfterEach
    void tearDown(){
        deliveryAddressRepository.deleteAllInBatch();
        cartItemRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    @DisplayName("요청에 아이디, 비밀번호 전달시 Authentication 객체가 반환된다.")
    @Test
    void attemptAuthentication()throws Exception{
        // Given
        String email = "kkk@naver.com";
        String password = "123";
        Member member = createMember(email,password);
        memberRepository.save(member);
        Map<String, Object> returnHm = new HashMap<>(){{
            put("email",    email);
            put("password", password);
        }};
        doReturn(returnHm).when(customLoginFilter).getBody(any());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // When
        Authentication expectedAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        Authentication actualAuthentication = customLoginFilter.attemptAuthentication(request, response);
        // Then
        assertNotNull(actualAuthentication);
        assertEquals(expectedAuthentication.getName(),actualAuthentication.getName());
        assertEquals(expectedAuthentication.getAuthorities(), actualAuthentication.getAuthorities());
        assertEquals(expectedAuthentication.getDetails(), actualAuthentication.getDetails());
    }
    @DisplayName("로그인 성공시 응답에 토큰과 사용자 정보가 담긴다.")
    @Test
    void successfulAuthentication()throws Exception{
        // Given
        String email    = "kkk@naver.com";
        String password = "123";
        Member member   = createMember(email,password);
        memberRepository.save(member);

        MockHttpServletRequest request      = new MockHttpServletRequest();
        MockHttpServletResponse response    = new MockHttpServletResponse();
        MockFilterChain filterChain         = new MockFilterChain();
        Authentication authentication       = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        // When
        customLoginFilter.successfulAuthentication(request, response, filterChain, authentication);
        // Then
        assertThat(response)
                .satisfies(res->{
                    // 쿠키 체크
                    for(Cookie cookie : res.getCookies()){
                        assertEquals(cookie.getName(), "accessToken");
                    }
                    assertEquals(res.getStatus(), ErrorType.LOGIN_SUCCESS.getStatusCode());

                    Message msg = new ObjectMapper().readValue(res.getContentAsString(), Message.class);
                    HashMap<String, String> loginDTO = (HashMap<String,String>)msg.getData();
                    assertEquals(msg.getStatus(),ErrorType.LOGIN_SUCCESS);
                    assertEquals(msg.getMessage(),ErrorType.LOGIN_SUCCESS.getErrStr());
                    assertEquals(loginDTO.get("email"),email);
                    assertEquals(loginDTO.get("master"),false);
                });
    }
    @DisplayName("로그인 실패시 응답에 ErrorType.LOGIN_FAILED가 담긴다.")
    @Test
    void unsuccessfulAuthentication()throws Exception{
        // Given
        MockHttpServletRequest request      = new MockHttpServletRequest();
        MockHttpServletResponse response    = new MockHttpServletResponse();
        AuthenticationException failed      = new AuthenticationException("Authentication failed") {};
        // When
        customLoginFilter.unsuccessfulAuthentication(request, response, failed);
        // Then
        assertThat(response)
                .satisfies(res->{
                   assertEquals(res.getStatus(), ErrorType.LOGIN_FAILED.getStatusCode());

                    Message msg = new ObjectMapper().readValue(res.getContentAsString(), Message.class);
                    assertEquals(msg.getStatus(),ErrorType.LOGIN_FAILED);
                    assertEquals(msg.getMessage(),ErrorType.LOGIN_FAILED.getErrStr());
                });
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