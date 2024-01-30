package com.practice_back.api;

import com.practice_back.dto.MemberDTO;
import com.practice_back.entity.Authority;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController // @Controller와 @ResponseBody 어노테이션의 기능 결합
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthServiceImpl authServiceImpl;
    /*
    * 회원가입
    *
    * @return 회원가입 결과를 담은 ResponseEntity
    * */
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody MemberDTO memberDTO, HttpServletResponse response){

        if(authServiceImpl.existsByEmail(memberDTO.getEmail()))
        {
            /*이메일이 이미 있는 경우*/
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new Message(ErrorType.DUPLICATE_EMAIL, "이미 가입된 이메일입니다.", null));
        }
        /* 사용자 데이터 저장 */
        memberDTO.setAuthority(Authority.ROLE_USER);
        MemberDTO resultDTO = authServiceImpl.signup(memberDTO, response);

        /*
        * 생성된 사용자에 대한 URI를 생성함. 보통의 관행을 따라함. 이 URI는 생성된 사용자의 고유 리소스를 가리킴
        * 여기서는 사용자의 이메일을 path 변수로 사용하여 URI를 구성, 프론트에서 생성된 경로로 리다이렉트 할 수 있음 ex)window.location.href = response.headers.location;
        * */
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath() // 현재 컨텍스트 경로(예: http://localhost:8080)를 기준으로
                .path("/api/user/member/{email}")// 경로를 설정합니다. {email}은 path 변수
                .buildAndExpand(resultDTO.getEmail())// path 변수 {username}에 실제 사용자 이름을 바인
                .toUri();// 최종적으로 URI 객체를 생성

        return ResponseEntity.created(location) // 생성된 리소스에 대한 참조로 URI를 포함하여 201 Created 응답을 반환
                .body(new Message(ErrorType.SIGNUP_SUCCESS, "회원가입 성공",resultDTO));

    }
}
