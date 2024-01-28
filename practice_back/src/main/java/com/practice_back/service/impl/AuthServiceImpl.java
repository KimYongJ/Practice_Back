package com.practice_back.service.impl;

import com.practice_back.dto.MemberDTO;
import com.practice_back.entity.Authority;
import com.practice_back.entity.Member;
import com.practice_back.jwt.TokenProvider;
import com.practice_back.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final MemberServiceImpl memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    /*
    * 회원 가입시 유효성에 어긋나는 필드와 메시지 출력 함수
    *
    * @return Map<String, String>
    * */
    @Override
    public Map<String, String> validateHandling(Errors errors)
    {
        Map<String, String> errorMap = new HashMap<>();
        /* 유효성 검사에 실패한 필드의 목록을 받음 */
        for(FieldError error : errors.getFieldErrors())
        {
            String fieldName = error.getField();
            errorMap.put(fieldName , error.getDefaultMessage());
        }
        return errorMap;
    }
    /*
    * DTO를 Entity로 변경 후 저장하는데 이 때 password는 암호화 한 상태로 저장한다.
    * */
    @Override
    public MemberDTO signup(MemberDTO memberDTO,  HttpServletResponse response)
    {
        Member memeber = memberService.save( memberDTO.toMemberSignUp(passwordEncoder) );

        String accessToken = tokenProvider.ceateAccessToken(memeber.getEmail(), Authority.ROLE_USER.name()); // email을 통해 사용자의 권한을 가져와 accessToken을 생성
        tokenProvider.saveCookie(response,"accessToken",accessToken); // 응답에 토큰을 저장한다.

        return memeber.of(memeber);
    }

    @Override
    public boolean existsByEmail(String email)
    {
        return memberService.existsByEmail(email);
    }
}
