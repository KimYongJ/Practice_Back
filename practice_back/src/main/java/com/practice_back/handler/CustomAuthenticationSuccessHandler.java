package com.practice_back.handler;

import com.practice_back.entity.Oauth2.Oauth2UserInfo;
import com.practice_back.jwt.TokenProvider;
import com.practice_back.service.impl.FactoryUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.fronturl}")
    private String frontUrl;

    private final TokenProvider     tokenProvider;
    private final FactoryUserInfo   factoryUserInfo;
    /**
     * 주석 1
     * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();// Authentication 객체로부터 권한 정보 get
        String authorityString = authorities.isEmpty() ? "" : authorities.iterator().next().getAuthority();// 권한 정보가 비어있지 않은 경우, 첫 번째 권한을 String으로 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Oauth2UserInfo oauth2UserInfo = factoryUserInfo.makeOauth2Userinfo(registrationId,oAuth2User);
        String ID = oauth2UserInfo.getProviderId().concat( oauth2UserInfo.getProvider() );

        String accessToken = tokenProvider.createAccessToken(ID, authorityString); // email을 통해 사용자의 권한을 가져와 accessToken을 생성
        tokenProvider.saveCookie(response, "accessToken", accessToken, 1); // 응답에 토큰을 저장
        response.setStatus(HttpServletResponse.SC_OK);
        getRedirectStrategy().sendRedirect(request, response, frontUrl);          // 로그인 성공 후 메인페이지 리디렉션
        /** 주석 2*/
    }
}

/**
 * 주석 1
 * [ OAuth2 로그인에서 Authentication 객체 생성 ]
 * OAuth2 로그인 프로세스에서 사용자가 외부 인증 제공자를 통해 성공적으로 인증을 완료하면,
 * OAuth2UserServiceImpl 클래스의 loadUser 함수의 반환 값(OAuth2User)이 Authentication의 principal로 설정됩니다
 * Authentication 객체는 사용자의 주요 정보(예: 이름, 이메일, 프로필 사진 URL 등)를 포함할 수 있습니다.
 *
 * 주석 2
 * 사용자가 원래 요청한 url을 찾고싶을 때 사용하는 함수 예시 : String targetUrl = determineTargetUrl(request, response, authentication)
 * */