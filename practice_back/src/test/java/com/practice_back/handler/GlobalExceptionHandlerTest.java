package com.practice_back.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice_back.dto.MemberDTO;
import com.practice_back.exception.InvalidImageFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    GlobalExceptionHandler globalExceptionHandler;
    @BeforeEach
    void setUp(){
        globalExceptionHandler = new GlobalExceptionHandler();
    }
    @DisplayName("회원 가입시 필수 입력사항을 입력하지 않으면, BAD_REQUEST와 함께 에러 객체가 반환된다.")
    @Test
    void handleValidationExceptions()throws Exception{
        // Given
        MemberDTO memberDTO = MemberDTO.builder()
                                .email("1")
                                .password("1")
                                .phoneNumber("1")
                                .build();
        // When Then
        mockMvc.perform(
                        post("/api/auth/signup")
                        .content(objectMapper.writeValueAsString(memberDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) // 요청이 어떻게 날아갔는지 확인
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("유효성 검증 실패"))
                .andExpect(jsonPath("$.data.password").value("비밀번호는 8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
                .andExpect(jsonPath("$.data.email").value("이메일 형식이 올바르지 않습니다."));
    }
    @DisplayName("요청 메서드가 지원되지 않을 때 METHOD_NOT_ALLOWED 오류가 반환된다.")
    @Test
    void handleMethodNotSupported()throws Exception{
        // Given  When Then
        mockMvc.perform(
                get("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().string("잘못된 요청입니다.GET"));
    }
    @DisplayName("요청의 데이터 형식이 잘못되었을 때 ")
    @Test
    void handleMessageNotReadable()throws Exception{
        // Given
        String inValidJson = "{\"username\":\"john\""; // 닫는 괄호 누락
        // When Then
        mockMvc.perform(
                    post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(inValidJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("잘못된 요청 데이터입니다."));
    }
    @DisplayName("데이터베이스 제약 조건 위반시 CONFLICT가 반환된다.")
    @Test
    void handleDataIntegrityViolation(){
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("제약조건위반");
        // When
        ResponseEntity<String> result = globalExceptionHandler.handleDataIntegrityViolation(exception);
        // Then
        assertThat(result)
                .satisfies(res->{
                   assertEquals(res.getStatusCode(), HttpStatus.CONFLICT);
                   assertEquals(res.getBody(),"잘못된 요청입니다.");
                });
    }
    @DisplayName("JPA 엔티티를 찾지 못했을 때 NOT_FOUND이 반환된다.")
    @Test
    void handleEntityNotFound(){
        // Given
        EntityNotFoundException exception = new EntityNotFoundException();
        // When
        ResponseEntity<String> result = globalExceptionHandler.handleEntityNotFound(exception);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(), HttpStatus.NOT_FOUND);
                    assertEquals(res.getBody(),"잘못된 요청입니다.");
                });
    }
    @DisplayName("일반적인 서버 오류는 500에러를 반환한다.")
    @Test
    void handleAllExceptions(){
        // Given
        Exception exception = new Exception();
        // When
        ResponseEntity<String> result = globalExceptionHandler.handleAllExceptions(exception);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
                    assertEquals(res.getBody(),"오류가 발생했습니다.");
                });
    }
    @DisplayName("입출력 예외시 500에러를 반환한다.")
    @Test
    void handleIOException(){
        // Given
        IOException exception = new IOException("I/O error");
        // When
        ResponseEntity<String> result = globalExceptionHandler.handleIOException(exception);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
                    assertEquals(res.getBody(),"An error occurred: I/O error");
                });
    }
    @DisplayName("")
    @Test
    void handleInvalidImageFile(){
        // Given
        InvalidImageFileException exception = new InvalidImageFileException("Invalid Image Format");
        // When
        ResponseEntity<String> result = globalExceptionHandler.handleInvalidImageFile(exception);
        // Then
        assertThat(result)
                .satisfies(res->{
                    assertEquals(res.getStatusCode(),HttpStatus.BAD_REQUEST);
                    assertEquals(res.getBody(),"Invalid Image Format");
                });
    }
}