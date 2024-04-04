package com.practice_back.service;

import com.practice_back.dto.LoginDTO;
import com.practice_back.dto.MemberDTO;
import com.practice_back.dto.UserProfileDTO;
import com.practice_back.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public interface AuthService {
    public MemberDTO signup(MemberDTO memberDTO,  HttpServletResponse response);
    public boolean existsByEmail(String email);
    public ResponseEntity<Object> gettemptoken(HttpServletResponse response, LoginDTO loginDTO);
    public ResponseEntity<Object> validateTmpToken(HttpServletRequest request);

    public Optional<Member> findByEmail(String email);
}
