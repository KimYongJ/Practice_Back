package com.practice_back.service;

import org.springframework.http.ResponseEntity;

public interface EmailAuthService {
    public ResponseEntity<Object> sendNewPassword(String email);

}
