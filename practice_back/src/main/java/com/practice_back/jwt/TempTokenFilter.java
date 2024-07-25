package com.practice_back.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TempTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public TempTokenFilter(TokenProvider tokenProvider){this.tokenProvider = tokenProvider;}
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().startsWith("/api/user/member") && "PATCH".equalsIgnoreCase(request.getMethod())){
            String tmpToken = tokenProvider.getToken(request, "tempToken");
            if(tmpToken == null){
                response.setStatus(HttpServletResponse.SC_SEE_OTHER);
                response.setHeader("Location", "checkuser"); // 리다이렉트 URL 설정
                response.setContentType("application/json");
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
