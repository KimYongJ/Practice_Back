package com.practice_back.jwt;

import com.practice_back.entity.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AccessTokenFilterTest {
    @Autowired
    TokenProvider tokenProvider;
    AccessTokenFilter accessTokenFilter;
    @BeforeEach
    void setUp(){
        accessTokenFilter = new AccessTokenFilter(tokenProvider);
    }
    @DisplayName("accessToken의 유효성을 검증한 후 SecurityContextHolder에 인증정보를 저장한다.")
    @Test
    void doFilterInternal()throws Exception{
        // Given
        MockHttpServletRequest  request  = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        String email = "kkk@naver.com";
        String tokenName = "accessToken";
        String tokenValue = tokenProvider.createAccessToken(email,Authority.ROLE_USER.name());
        Cookie cookie = new Cookie(tokenName, tokenValue);
        request.setCookies(cookie);
        // When
        Authentication expectedAuthentication = tokenProvider.getAuthentication(request, tokenValue);
        accessTokenFilter.doFilterInternal(request, response, filterChain);
        Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();
        // Then
        assertNotNull(actualAuthentication);
        assertEquals(expectedAuthentication.getName(),actualAuthentication.getName());
        assertEquals(expectedAuthentication.getAuthorities(), actualAuthentication.getAuthorities());
        assertEquals(expectedAuthentication.getDetails(), actualAuthentication.getDetails());
    }
}