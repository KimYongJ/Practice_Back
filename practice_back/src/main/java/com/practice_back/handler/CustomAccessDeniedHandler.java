package com.practice_back.handler;

import com.practice_back.response.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.practice_back.handler.HandlerFunc.handlerException;

@Component
/*
* [AccessDeniedHandler]
* - 사용자가 인증은 되었으나 해당 리소스에 접근할 수 있는 권한이 없을 때 사용됨
*
* */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException){
        handlerException(response, ErrorType.NOT_FOUND);
    }
}