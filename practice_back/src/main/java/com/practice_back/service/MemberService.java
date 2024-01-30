package com.practice_back.service;

import com.practice_back.dto.MemberDTO;
import com.practice_back.entity.Member;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface MemberService {
    boolean existsByEmail(String email);
    Member save(Member memeber);
    Optional<Member> findByEmail(String email);
    public ResponseEntity<Object> deleteByEmail();
}
