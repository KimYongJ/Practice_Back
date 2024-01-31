package com.practice_back.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
@Slf4j
public class HandlerFunc {

    public static void handlerException(HttpServletResponse response, ErrorType error)
    {

        try {
            response.setStatus(error.getStatusCode());
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String json = new ObjectMapper().writeValueAsString(new Message(error, error.getErrStr(), "1"));
            response.getWriter().write(json);
            response.flushBuffer();       // 응답을 클라이언트에 전송
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    public static void handlerException(HttpServletResponse response, ErrorType error,Object object)
    {

        try {
            response.setStatus(error.getStatusCode());
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String json = new ObjectMapper().writeValueAsString(new Message(error, error.getErrStr(), object));
            response.getWriter().write(json);
            response.flushBuffer();       // 응답을 클라이언트에 전송
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
