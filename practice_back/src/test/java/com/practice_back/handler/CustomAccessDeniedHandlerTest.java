package com.practice_back.handler;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.response.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomAccessDeniedHandlerTest {
    @Autowired
    MockMvc mockMvc;
    @DisplayName("권한이 없는 요청일 때 NOT_FOUND가 반환된다.")
    @Test
    @WithMockCustomUser(roles = "USER", username="kyj", password="1")
    void handle() throws Exception{
        // Given /api/user/items/1
        mockMvc.perform(
                delete("/api/user/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("NOT_FOUND"))
                .andExpect(jsonPath("$.data").value("1"));
        // When

        // Then
    }
}