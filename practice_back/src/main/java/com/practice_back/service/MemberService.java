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
    boolean existsByEmail(String email);
    Member save(Member memeber);
    Optional<Member> findByEmail(String email);
    public ResponseEntity<Object> deleteByEmail(HttpServletRequest request, HttpServletResponse response);
    public ResponseEntity<Object> getUserProfile();
    public ResponseEntity<Object> updateProfile(UserProfileDTO userProfileDTO);
    public ResponseEntity<Object> updatePassword(PasswordDTO passwordDTO);
}
