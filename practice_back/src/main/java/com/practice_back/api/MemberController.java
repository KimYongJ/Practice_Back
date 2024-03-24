package com.practice_back.api;

import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.MemberService;
import com.practice_back.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Controller와 @ResponseBody 어노테이션의 기능 결합
@RequestMapping("/api/user/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 탈퇴
     *
     * @return ResponseEntity<Object>
     * */
    @DeleteMapping()
    public ResponseEntity<Object> deleteUser(){
        return memberService.deleteByEmail();
    }
}
