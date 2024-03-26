package com.practice_back.service;

import org.springframework.http.ResponseEntity;

import java.util.Random;

public interface EmailAuthService {
    public ResponseEntity<Object> sendNewPassword(String email);
    public String generateRandomPassword();
    public String buildEmailContent(String password) ;
    public ResponseEntity<Object> sendEmail(String email, String subject, String content, String newPassword);
}
