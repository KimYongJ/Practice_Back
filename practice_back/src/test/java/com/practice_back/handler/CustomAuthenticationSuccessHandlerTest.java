package com.practice_back.handler;

import com.practice_back.entity.Oauth2.Oauth2UserInfo;
import com.practice_back.factory.FactoryUserInfo;
import com.practice_back.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomAuthenticationSuccessHandlerTest {
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    TokenProvider tokenProvider;
    @MockBean
    FactoryUserInfo factoryUserInfo;
    Authentication authentication;
    String location = "http://localhost:3000/authsuccess";
    @BeforeEach
    void setUp(){
        OAuth2AuthenticationToken oauthToken = mock(OAuth2AuthenticationToken.class);
        Oauth2UserInfo oauth2UserInfo = mock(Oauth2UserInfo.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        authentication = mock(OAuth2AuthenticationToken.class);

        doReturn("GOOGLE").when((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId();
        doReturn(Arrays.asList(new SimpleGrantedAuthority("ROLE_USE"))).when(authentication).getAuthorities();
        doReturn(oAuth2User).when(authentication).getPrincipal();
        doReturn(oauth2UserInfo).when(factoryUserInfo).makeOauth2Userinfo(any(),any());
        doReturn("dummy1").when(oauth2UserInfo).getProviderId();
        doReturn("dummy2").when(oauth2UserInfo).getProvider();

        customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(tokenProvider,factoryUserInfo);
        ReflectionTestUtils.setField(customAuthenticationSuccessHandler, "frontUrl", location);
    }
    @DisplayName("로그인 성공시 메인 페이지로 리디렉션 된다.")
    @Test
    void onAuthenticationSuccess()throws Exception{
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String cookieName = "accessToken";
        // When
        customAuthenticationSuccessHandler.onAuthenticationSuccess(request,response,authentication);
        // Then
        assertThat(response)
                .satisfies(res->{
                    assertEquals(res.getStatus(), HttpServletResponse.SC_MOVED_TEMPORARILY);
                    Cookie cookie = res.getCookie(cookieName);
                    assertEquals(cookie.getName(), cookieName);
                    assertEquals(res.getHeader("Location"),location);
                });
    }
}