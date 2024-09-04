package com.practice_back.handler;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.response.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationEntryPointHandlerTest {
    @Autowired
    MockMvc mockMvc;
    @DisplayName("인증 없이 보호된 리소스에 접근할 때 BAD_REQUEST가 반환된다.")
    @Test
    void handle() throws Exception{
        // Given When Then
        mockMvc.perform(
                        delete("/api/user/items/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorType.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(ErrorType.BAD_REQUEST.getErrStr()))
                .andExpect(jsonPath("$.data").value("1"));
    }
}