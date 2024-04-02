package com.practice_back.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Controller와 @ResponseBody 어노테이션의 기능 결합
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class Oauth2Controller {
    @PostMapping("/code/google")
    public ResponseEntity<Object> googlelogin() {
        return null;
    }
}
