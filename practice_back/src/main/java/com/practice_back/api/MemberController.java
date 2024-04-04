package com.practice_back.api;

import com.practice_back.dto.PasswordDTO;
import com.practice_back.dto.UserProfileDTO;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.MemberService;
import com.practice_back.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController                     //@Controller와 @ResponseBody 어노테이션의 기능 결합
@RequestMapping("/api/user/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    /**
     * 회원 정보 조회
     *
     * @return ResponseEntity<Object>
     * */
    @GetMapping()
    public ResponseEntity<Object> getUserProfile(){
        return memberService.getUserProfile();
    }

    /**
     * 회원 정보 업데이트
     *
     * @return ResponseEntity<Object>
     * */
    @PatchMapping()
    public ResponseEntity<Object> updateProfile(HttpServletRequest request, @Valid @RequestBody UserProfileDTO userProfileDTO){
        return memberService.updateProfile(request,userProfileDTO);
    }

    /**
     * 비밀번호 변경
     *
     * @return ResponseEntity<Object>
     * */
    @PatchMapping("/changpwd")
    public ResponseEntity<Object> updatePassword(HttpServletRequest request, @Valid @RequestBody PasswordDTO passwordDTO){
        return memberService.updatePassword(request,passwordDTO);
    }

    /**
     * 회원 탈퇴
     *
     * @return ResponseEntity<Object>
     * */
    @DeleteMapping()
    public ResponseEntity<Object> deleteUser(HttpServletRequest request, HttpServletResponse response){
        return memberService.deleteByEmail(request, response);
    }


}
