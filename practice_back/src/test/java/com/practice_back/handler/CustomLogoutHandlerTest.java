package com.practice_back.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice_back.entity.Authority;
import com.practice_back.jwt.TokenProvider;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.Cookie;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomLogoutHandlerTest {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    CustomLogoutHandler customLogoutHandler;
    @Autowired
    ObjectMapper objectMapper;
    @DisplayName("인증된 사용자가 로그아웃을 할 경우 컨텍스트 정보, 쿠키정보가 리셋되고 LOGOUT_SUCCESS 결과가 반환 된다.")
    @Test
    void logout(){
        // Given
        String cookieName = "accessToken";
        String tokenValue = tokenProvider.createAccessToken("kkk@naver.com",Authority.ROLE_USER.name(), new Date());

        Authentication authentication = tokenProvider.getAuthentication(tokenValue);

        SecurityContextHolder.getContext().setAuthentication(authentication); // 컨텍스트 홀더에 인증 정보 저장

        Authentication expectedAuthentication = SecurityContextHolder.getContext().getAuthentication();// 저장한 인증 정보 확인

        Cookie expectedCookie = new Cookie(cookieName, tokenValue);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(expectedCookie);
        // When
        customLogoutHandler.logout(request, response, expectedAuthentication);
        // Then

        // 인증 정보 리셋 검증
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotEquals(SecurityContextHolder.getContext().getAuthentication(), expectedAuthentication);

        // 쿠키 검증
        assertNotEquals(expectedCookie.getMaxAge(), 0);
        assertThat(response)
                .satisfies(res->{
                    Cookie cookie = res.getCookie(cookieName);
                    assertEquals(cookie.getName(), cookieName);
                    assertTrue(cookie.getMaxAge() == 0);
                });

        // 반환 값 검증
        ErrorType errorType = ErrorType.LOGOUT_SUCCESS;
        assertThat(response)
                .satisfies(res->{
                    assertEquals(res.getStatus(),  errorType.getStatusCode());

                    Message msg = objectMapper.readValue(res.getContentAsString(),Message.class);
                    assertEquals(msg.getStatus(),errorType);
                    assertEquals(msg.getMessage(),errorType.getErrStr());
                });
    }
    @DisplayName("인증되지 않은 사용자일 경우 응답에 UNAUTHORIZED 값이 담긴다.")
    @Test
    void invalidateLogout(){
        // Given
        String tokenValue = tokenProvider.createAccessToken("kkk@naver.com", Authority.ROLE_USER.name(), new Date());
        Authentication authentication = tokenProvider.getAuthentication(tokenValue);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        // When
        customLogoutHandler.logout(request, response, authentication);
        // Then
        ErrorType errorType = ErrorType.UNAUTHORIZED;
        assertThat(response)
                .satisfies(res->{
                    assertEquals(res.getStatus(),  errorType.getStatusCode());

                    Message msg = objectMapper.readValue(res.getContentAsString(),Message.class);
                    assertEquals(msg.getStatus(),errorType);
                    assertEquals(msg.getMessage(),errorType.getErrStr());
                });
    }
}