package com.practice_back.handler;

import com.practice_back.jwt.TokenProvider;
import com.practice_back.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.practice_back.handler.HandlerFunc.handlerException;
import static com.practice_back.jwt.TokenProvider.contextReset;

@Component
@RequiredArgsConstructor
/*
* [LogoutHandler]
*  - 스프링 시큐리티는 /logout url로 들어오는 post요청에 대해 자동으로 filterChain의 .logout()을 실행한다.
*  - 다른 url로 오는 것을 설정하기 원하면 .logout().logoutUrl("/custom-logout")과 같이 별도의 url을 지정한다.
* */
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenProvider tokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 인증 객체 검증
        String accessToken = tokenProvider.getToken(request,"accessToken");                                 // 사용자 요청으로부터 엑세스 토큰을 가져온다.
        if(accessToken != null && accessToken.length()>11){
            // 로그아웃 로직
            tokenProvider.cookieReset(response,"accessToken");
            contextReset(); // 인증 리셋
            handlerException(response, ErrorType.LOGOUT_SUCCESS);
        }else{
            // 인증 되지 않은 사용자일 경우
            handlerException(response, ErrorType.UNAUTHORIZED);
        }

    }
}