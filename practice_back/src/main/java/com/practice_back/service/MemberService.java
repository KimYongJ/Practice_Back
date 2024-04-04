package com.practice_back.service;

import com.practice_back.dto.MemberDTO;
import com.practice_back.dto.PasswordDTO;
import com.practice_back.dto.UserProfileDTO;
import com.practice_back.entity.Member;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface MemberService {
    public ResponseEntity<Object> deleteByEmail(HttpServletRequest request, HttpServletResponse response);
    public ResponseEntity<Object> getUserProfile();
    public ResponseEntity<Object> updateProfile(HttpServletRequest request,UserProfileDTO userProfileDTO);
    public ResponseEntity<Object> updatePassword(HttpServletRequest request,PasswordDTO passwordDTO);
}
