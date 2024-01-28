package com.practice_back.handler;

import com.practice_back.response.ErrorType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.practice_back.handler.HandlerFunc.handlerException;

/*
* [AuthenticationEntryPoint]
* - 사용자가 인증되지 않은 상태에서 보호된 리소스에 접근하려고 할 때 사용됨
* */
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException){
        handlerException(response, ErrorType.BAD_REQUEST); // 인증 되지 않은 상태에서 보호된 리소스에 접근할 때
    }


}
