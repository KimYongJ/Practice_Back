package com.practice_back.service;

import com.practice_back.dto.MemberDTO;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AuthService {
    public MemberDTO signup(MemberDTO memberDTO,  HttpServletResponse response);
    public boolean existsByEmail(String email);
}
