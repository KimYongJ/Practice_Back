package com.practice_back.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.entity.Authority;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.Cookie;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {
    @Autowired
    TokenProvider tokenProvider;
    @Value("${token.secret}")
    String key;
    @Value("${token.accessTokenExpireTime}")
    private Integer ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${token.tempTokenExpireTime}")
    private Integer TEMP_TOKEN_EXPIRE_TIME;
    @DisplayName("이메일과 권한을 저장하면 JWT문자열이 반환된다.")
    @Test
    void createAccessToken(){
        // Given
        String email = "kkk@naver.com";
        String auth = Authority.ROLE_USER.name();
        Date now = new Date();
        // When
        String accessToken = tokenProvider.createAccessToken(email, auth, now);
        // Then
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(accessToken)
                .getBody();

        long expected = now.getTime() + ACCESS_TOKEN_EXPIRE_TIME;
        long actual = claims.getExpiration().getTime();

        assertEquals(email, claims.getSubject());
        assertEquals(auth,claims.get("auth key"));
        assertTrue(Math.abs(expected - actual) < 1000);
    }
    @DisplayName("이메일 전달시 임시 토큰이 발급된다.")
    @Test
    void createTempToken(){
        // Given
        String email = "kkk@naver.com";
        String auth = Authority.ROLE_USER.name();
        Date now = new Date();
        // When
        String tempToken = tokenProvider.createTempToken(email,auth, now);
        // Then
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(tempToken)
                .getBody();

        long expected = now.getTime() + TEMP_TOKEN_EXPIRE_TIME;
        long actual = claims.getExpiration().getTime();

        assertEquals(email, claims.getSubject());
        assertEquals("ROLE_USER",claims.get("auth key"));
        assertTrue(Math.abs(expected - actual) < 1000);
    }
    @DisplayName("응답과, 쿠키 이름, 유효시간 플레그를 전달하면 응답에 쿠키가 담긴다. 엑세스토큰 테스트")
    @Test
    void setAccessTokenCookie(){
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String cookieName = "accessCookie";
        String token = tokenProvider.createAccessToken("", "", new Date());
        int flag = 1; // 엑세스 토큰
        // When
        tokenProvider.saveCookie(response, cookieName, token, flag);
        // Then
        assertThat(response)
                .satisfies(res->{
                    Cookie cookie = res.getCookie(cookieName);
                    assertEquals(cookie.getName(), cookieName);
                    assertTrue(cookie.getMaxAge() == ACCESS_TOKEN_EXPIRE_TIME/1000);
                });

    }
    @DisplayName("응답과, 쿠키 이름, 유효시간 플레그를 전달하면 응답에 쿠키가 담긴다. 템프토큰 테스트")
    @Test
    void setTempTokenCookie(){
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String cookieName = "tempCookie";
        String token = tokenProvider.createAccessToken("", "", new Date());
        int flag = 2; // 엑세스 토큰
        // When
        tokenProvider.saveCookie(response, cookieName, token, flag);
        // Then
        assertThat(response)
                .satisfies(res->{
                    Cookie cookie = res.getCookie(cookieName);
                    assertEquals(cookie.getName(), cookieName);
                    assertTrue(cookie.getMaxAge() == TEMP_TOKEN_EXPIRE_TIME/1000);
                });
    }
    @DisplayName("토큰의 유효시간이 지나지 않았다면 true를 반환 한다.")
    @Test
    void validateToken() throws Exception{
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String email = "kkk@naver.com";
        String auth = Authority.ROLE_USER.name();
        Date now = new Date();
        String accessToken = tokenProvider.createAccessToken(email, auth, now);
        // When
        boolean flag = tokenProvider.validateToken(response, accessToken);
        // Then
        assertTrue(flag);
    }
    @DisplayName("토큰의 유효시간이 지났다면 응답에 ErrorType.EXPIRED_TOKEN 결과가 담긴다.")
    @Test
    void testExpiredTokenValidation()throws Exception{
        // Given
        Calendar calendar  = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        MockHttpServletResponse response = new MockHttpServletResponse();
        String email = "kkk@naver.com";
        String auth = Authority.ROLE_USER.name();
        Date now = calendar.getTime();
        String accessToken = tokenProvider.createAccessToken(email, auth, now);
        // When
        tokenProvider.validateToken(response, accessToken);
        // Then
        assertThat(response)
                .satisfies(res->{
                   assertEquals(res.getStatus(), ErrorType.EXPIRED_TOKEN.getStatusCode());
                   Message message = new ObjectMapper().readValue(res.getContentAsString(), Message.class);
                   assertEquals(message.getStatus(), ErrorType.EXPIRED_TOKEN);
                   assertEquals(message.getMessage(), ErrorType.EXPIRED_TOKEN.getErrStr());
                });
    }
    @DisplayName("토큰이 유효하지 않은 구조일 때 ErrorType.BAD_REQUEST를 반환 한다.")
    @Test
    void testInvalidTokenStructure()throws Exception{
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String accessToken = "";
        // When
        tokenProvider.validateToken(response, accessToken);
        // Then
        assertThat(response)
                .satisfies(res->{
                    assertEquals(res.getStatus(), ErrorType.BAD_REQUEST.getStatusCode());
                    Message message = new ObjectMapper().readValue(res.getContentAsString(), Message.class);
                    assertEquals(message.getStatus(), ErrorType.BAD_REQUEST);
                    assertEquals(message.getMessage(), ErrorType.BAD_REQUEST.getErrStr());
                });
    }
    @DisplayName("Temp 토큰이 유효하다면 true를 반환한다.")
    @Test
    void validateTmpToken(){
        // Given
        String email = "kkk@naver.com";
        String auth = Authority.ROLE_USER.name();
        Date now = new Date();
        String tempToken = tokenProvider.createTempToken(email,auth, now);
        // When
        boolean bool = tokenProvider.validateTmpToken(tempToken);
        // Then
        assertTrue(bool);
    }
    @DisplayName("Temp 토큰이 유효하지 않다면 false를 반환한다.")
    @Test
    void InvalidateTmpToken(){
        // Given When
        boolean bool = tokenProvider.validateTmpToken("");
        // Then
        assertFalse(bool);
    }
    @DisplayName("토큰 전달시 Authentication 객체가 반환 된다.")
    @Test
    void getAuthentication(){
        // Given
        String email        = "kkk@naver.com";
        String auth         = Authority.ROLE_USER.name();
        Date now            = new Date();
        String accessToken  = tokenProvider.createAccessToken(email, auth, now);
        // When
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        // Then
        assertNotNull(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        List<String> actualAuthorities = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
        assertEquals(email, userDetails.getUsername());
        assertEquals(auth, actualAuthorities.get(0));
    }
    @DisplayName("토큰 이름과 토큰이 담긴 사용자 요청을 전달하면 토큰을 가져올 수 있다.")
    @Test
    void getToken(){
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String email        = "kkk@naver.com";
        String auth         = Authority.ROLE_USER.name();
        Date now            = new Date();
        String tokenName    = "accessToken";
        String accessToken  = tokenProvider.createAccessToken(email, auth, now);
        request.setCookies(getCookie(tokenName, accessToken));
        // When
        String actualToken = tokenProvider.getToken(request, tokenName);
        // Then
        assertEquals(accessToken, actualToken);
    }
    @DisplayName("응답에 쿠키의 유효시간을 0으로 설정하여 반환한다.")
    @Test
    void cookieReset(){
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String cookieName = "accessCookie";
        String token = tokenProvider.createAccessToken("", "", new Date());
        int flag = 1; // 엑세스 토큰

        // When Then
        tokenProvider.saveCookie(response, cookieName, token, flag);
        assertThat(response)
                .satisfies(res->{
                    Cookie cookie = res.getCookie(cookieName);
                    assertEquals(cookie.getName(), cookieName);
                    assertTrue(cookie.getMaxAge() == ACCESS_TOKEN_EXPIRE_TIME/1000);
                });

        tokenProvider.cookieReset(response,cookieName);
        assertThat(response)
                .satisfies(res->{
                    Cookie cookie = res.getCookie(cookieName);
                    assertEquals(cookie.getName(), cookieName);
                    assertTrue(cookie.getMaxAge() == 0);
                });
    }
    @DisplayName("컨텍스트홀더에서 로그인한 사용자 정보를 가져온다.")
    @Test
    @WithMockCustomUser(username="kyj", password="1")
    void getCurrentMemberInfo(){
        // Given When
        String memberInfo = tokenProvider.getCurrentMemberInfo();
        // Then
        assertEquals("kyj", memberInfo);
    }
    @DisplayName("컨텍스트 홀더 안의 데이터를 초기화 한다.")
    @Test
    void contextReset(){
        // Given
        String tokenValue = tokenProvider.createAccessToken("kkk@naver.com",Authority.ROLE_USER.name(), new Date());
        Authentication expectedAuthentication = tokenProvider.getAuthentication(tokenValue);

        // When Then
        SecurityContextHolder.getContext().setAuthentication(expectedAuthentication); // 컨텍스트 홀더에 인증 정보 저장
        Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();// 저장 한것을 다시 꺼내옴

        assertEquals(expectedAuthentication.getName(),actualAuthentication.getName());
        assertEquals(expectedAuthentication.getAuthorities(), actualAuthentication.getAuthorities());
        assertEquals(expectedAuthentication.getDetails(), actualAuthentication.getDetails());

        tokenProvider.contextReset();

        actualAuthentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(actualAuthentication);
    }
    public Cookie getCookie(String tokenName, String accessToken){
        return new Cookie(tokenName, accessToken);
    }
}