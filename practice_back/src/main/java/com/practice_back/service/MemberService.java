package com.practice_back.service;

import com.practice_back.dto.PasswordDTO;
import com.practice_back.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {
    public ResponseEntity<Object> deleteById(HttpServletResponse response);
    public ResponseEntity<Object> getUserProfile();
    public ResponseEntity<Object> updateProfile(UserProfileDTO userProfileDTO);
    public ResponseEntity<Object> updatePassword(PasswordDTO passwordDTO);
}
