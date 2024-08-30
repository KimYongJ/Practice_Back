package com.practice_back.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HandlerFuncTest {
    HandlerFunc handlerFunc;
    @BeforeEach
    void setUp(){
        handlerFunc = new HandlerFunc();
    }
    @DisplayName("응답과 에러 코드 전달시 응답에 에러 코드가 담긴다.")
    @Test
    void handlerException(){
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        ErrorType errorType = ErrorType.OK;
        // When
        handlerFunc.handlerException(response, errorType);
        // Then
        assertThat(response)
                .satisfies(res->{
                    assertEquals(res.getStatus(), errorType.getStatusCode());

                    Message msg = new ObjectMapper().readValue(res.getContentAsString(), Message.class);
                    assertEquals(msg.getStatus(), errorType);
                    assertEquals(msg.getMessage(), errorType.getErrStr());
                    assertEquals(msg.getData(), (String)msg.getData());
                });
    }
    @DisplayName("응답과, 에러코드, 객체 전달시 응답에 에러코드와 객체가 담긴다.")
    @Test
    void handlerExceptionUseObject(){
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        ErrorType errorType = ErrorType.OK;
        List<String> obj = List.of("하나","둘");
        // When
        handlerFunc.handlerException(response, errorType, obj);
        // Then
        assertThat(response)
                .satisfies(res->{
                    assertEquals(res.getStatus(), errorType.getStatusCode());

                    Message msg = new ObjectMapper().readValue(res.getContentAsString(), Message.class);
                    List<String> list = (List)msg.getData();
                    assertEquals(msg.getStatus(), errorType);
                    assertEquals(msg.getMessage(), errorType.getErrStr());
                    assertEquals(list.get(0), "하나");
                    assertEquals(list.get(1), "둘");
                });
    }
}