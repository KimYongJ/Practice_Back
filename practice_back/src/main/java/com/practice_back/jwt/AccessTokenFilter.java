package com.practice_back.jwt;


import com.practice_back.response.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * [ OncePerRequestFilter 상속 이유 ]
 * - OncePerRequestFilter는 스프링의 필터 체인 내에서 단 한 번만 실행되는 필터
 * - 이 클래스를 상속받으면, 각 HTTP 요청에 대해 필터의 로직이 한 번씩만 실행되도록 보장( 상속받지 않을 경우 필터가 요청마다 여러번 실행될 수 있음)
 * */
public class AccessTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AccessTokenFilter.class);
    private final TokenProvider tokenProvider;

    public AccessTokenFilter(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }
    /**
     * [ doFilterInternal 함수 ]
     * - 이 메소드는 HttpSecurity객체에 AccessTokenFilter클래스가 필터로 등록됬을 때 스프링 시큐리티 필터 체인의 일부로 작동하며 보안 관련 처리를 할 수 있다.
     * - 보통 예외는 @ControllerAdvice를 사용하여 전역 예외 핸들러를 구성하는데 이는 필터 체인 외부(컨틀롤러등..)에서만 적용되고 필터 체인 내에서 발생한 예외는
     *   자체적으로 처리해야 한다. 커스텀 필터에 요청에 대해 예외처리는 response 객체에 직접 응답을 구성한다. 보통 json 형식의 오류 메시지를 보낸다.
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = tokenProvider.getToken(request,"accessToken");                                 // 사용자 요청으로부터 엑세스 토큰을 가져온다.
        if (accessToken != null)
        {
            try {
                if (tokenProvider.validateToken(response, accessToken))// 토큰의 유효성을 검증한다.
                {
                    Authentication authentication = tokenProvider.getAuthentication(request, accessToken); // 사용자 인증 정보를 가져온다.
                    if (authentication != null) {                                                            // 인증 정보가 null이 아닐 경우 컨텍스트 홀더에 인증 정보를 저장 한다.
                        /**
                         * [ setAuthentication 사용 이유 ]
                         * - 이 메소드는 현재 스레드의 보안 컨텍스트에 Authentication 객체를 설정한다. 스레드 보안 컨텍스트에 Authentication객체 생성시 현재 요청과 관련된
                         *   인증정보가 스프링 시큐리티에 의해 관리되며 어플리케이션 다른 부분에서 현재 사용자의 인증 정보를 조회 할 수 있다.
                         *   setAuthentication를 사용해 Authentication을 설정하지 않으면, 현재 사용자 인증 정보가 조회되지 않아 결과적으로 보안을 적용할 수 없다.
                         * - setAuthentication를 사용해 설정된 인증 정보는 SecurityContextHolder를 통해 컨트롤러에서 Authentication 객체를 통해 접근할 수 있다.
                         * - 접근예시 : Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                      k   *			  String userId = authentication.getName();
                         * */
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                else
                {
                    return; // 유효성을 통과하지 못하면 응답종료
                }
            }catch(Exception e){
                request.setAttribute("exception", ErrorType.BAD_REQUEST);                           // 잘못된 요청
            }
        }
        /**
         * [ doFilter의 역할 ]
         * - 현재 필터 이후의 필터 체인을 계속 진행하도록 한다. 현재까지 진행된 필터 작업이 끝나고 다음 필터로 이동하며 필터가 더이상 없을 경우
         * 	해당 요청이 컨트롤러로 이동한다. 이 메소드를 호출하지 않으면 필터 체인이 중단되고 요청이 컨트롤러에 도달하지 않는다.
         * */
        filterChain.doFilter(request, response);
    }


}