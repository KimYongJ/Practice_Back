package com.practice_back.api;

import com.practice_back.dto.EmailDTO;
import com.practice_back.dto.LoginDTO;
import com.practice_back.dto.MemberDTO;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.AuthService;
import com.practice_back.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService       authService;
    private final EmailAuthService  emailAuthService;
    /**
    * 회원가입
    *
    * @return 회원가입 결과를 담은 ResponseEntity
    * */
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody MemberDTO memberDTO, HttpServletResponse response){

        if(authService.existsByEmail(memberDTO.getEmail()))
        {
            /*이메일이 이미 있는 경우*/
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new Message(ErrorType.DUPLICATE_EMAIL, "이미 가입된 이메일입니다.", null));
        }
        /* 사용자 데이터 저장 */
        MemberDTO resultDTO = authService.signup(memberDTO, response);

        /**
        * 생성된 사용자에 대한 URI를 생성함. 보통의 관행을 따라함. 이 URI는 생성된 사용자의 고유 리소스를 가리킴
        * 여기서는 사용자의 이메일을 path 변수로 사용하여 URI를 구성, 프론트에서 생성된 경로로 리다이렉트 할 수 있음 ex)window.location.href = response.headers.location;
        * */
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()               // 현재 컨텍스트 경로(예: http://localhost:8080)를 기준으로
                .path("/api/user/member/{email}")       // 경로를 설정합니다. {email}은 path 변수
                .buildAndExpand(resultDTO.getEmail())   // path 변수 {username}에 실제 사용자 이름을 바인
                .toUri();                               // 최종적으로 URI 객체를 생성

        return ResponseEntity.created(location)         // 생성된 리소스에 대한 참조로 URI를 포함하여 201 Created 응답을 반환
                .body(new Message(ErrorType.SIGNUP_SUCCESS, "회원가입 성공",resultDTO));

    }
    /**
    * 비밀번호 찾기
    *
    * @return 성공 유무를 담은 ResponseEntity
    * */
    @PostMapping("/newpassword")
    public ResponseEntity<Object> sendNewPassword(@RequestBody EmailDTO emailDto) {
        return emailAuthService.sendNewPassword(emailDto.getEmail());
    }

    /**
     * Temp Token 발급( 회원정보 수정시 사용 )
     *
     * @return 성공 유무를 담은 ResponseEntity
     * */
    @PostMapping("/temptoken")
    public ResponseEntity<Object> gettemptoken(HttpServletResponse response, @RequestBody LoginDTO loginDTO){
        return authService.gettemptoken(response, loginDTO);
    }
    /**
     * Temp Token 유효성 검사
     *
     * @return 성공 유무를 담은 ResponseEntity
     * */
    @GetMapping("/validatetmptoken")
    public ResponseEntity<Object> validateTmpToken(HttpServletRequest request){
        return authService.validateTmpToken(request);
    }
    /**
     * reCAPTCHA 유효성 검사
     *
     * @return 성공 유무를 담은 ResponseEntity
     * */
    @PostMapping("/recaptchaverify")
    public ResponseEntity<Object> verifyRecaptcha(@RequestBody Map<String, Object> recaptchaData){
        return authService.verifyRecaptcha(recaptchaData);
    }


}
