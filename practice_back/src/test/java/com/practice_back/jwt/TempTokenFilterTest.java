package com.practice_back.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import javax.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class TempTokenFilterTest {
    @Autowired
    TokenProvider tokenProvider;
    private TempTokenFilter tempTokenFilter;
    @BeforeEach
    void setUp(){
        tempTokenFilter = new TempTokenFilter(tokenProvider);
    }
    @DisplayName("/api/user/member 요청에 대한 수정일 경우 tempToken이 있어야 한다. 없다면 응답에 리다이렉트 코드가 온다")
    @Test
    void doFilterInternal()throws Exception{
        // Given
        MockFilterChain filterChain         = new MockFilterChain();
        MockHttpServletResponse response    = new MockHttpServletResponse();
        MockHttpServletRequest request      = new MockHttpServletRequest();
        request.setRequestURI("/api/user/member");
        request.setMethod("PATCH");
        // When
        tempTokenFilter.doFilterInternal(request, response, filterChain);
        // Then
        assertEquals(response.getHeader("Location"),"checkuser");
        assertEquals(response.getStatus(),HttpServletResponse.SC_SEE_OTHER);
    }
}