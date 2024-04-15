package com.practice_back.service.impl;

import com.practice_back.dto.LoginDTO;
import com.practice_back.dto.MemberDTO;
import com.practice_back.entity.Authority;
import com.practice_back.entity.Member;
import com.practice_back.jwt.TokenProvider;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    @Value("${token.recaptchaSecret}")
    private String                      recaptchaSecret;
    @Value("${token.recaptchaServerURL}")
    private String                      recaptchaServerURL;
    private final TokenProvider         tokenProvider;
    private final PasswordEncoder       passwordEncoder;
    private final MemberRepository      memberRepository;
    private final AuthenticationManager authenticationManager;
    @Override
    public MemberDTO signup(MemberDTO memberDTO,  HttpServletResponse response)
    {
        Member member = memberDTO.toMemberSignUp(Authority.ROLE_USER, passwordEncoder);  // password는 암호화 한 상태로 저장한다.
        member.getCart().setMember(member);
        memberRepository.save( member );
        String accessToken = tokenProvider.createAccessToken(member.getEmail(), Authority.ROLE_USER.name()); // email을 통해 사용자의 권한을 가져와 accessToken을 생성
        tokenProvider.saveCookie(response,"accessToken",accessToken, 1); // 응답에 토큰을 저장한다.
        return member.of(member);
    }
    @Override
    public ResponseEntity<Object> gettemptoken(HttpServletResponse response, LoginDTO loginDTO){
        // 정보가 맞는지 확인
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        try{
            authenticationManager.authenticate(authRequest);    // 없는 정보라면 exception 발생

            String tempToken = tokenProvider.createTempToken(loginDTO.getEmail());
            tokenProvider.saveCookie(response,"tempToken",tempToken, 2); // 응답에 토큰을 저장한다.
        }catch(Exception e){
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.INVALID_LOGIN_CREDENTIALS,ErrorType.INVALID_LOGIN_CREDENTIALS.getErrStr() , null));
        }
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .body(new Message(ErrorType.MOVED_PERMANENTLY, "확인 완료.","account"));
    }
    @Override
    public boolean existsByEmail(String Id)
    {
        return memberRepository.existsById(Id);
    }

    @Override
    public ResponseEntity<Object> validateTmpToken(HttpServletRequest request){
        ErrorType errorType;
        String tmpToken     = tokenProvider.getToken(request, "tempToken");
        boolean isValidate  = tokenProvider.validateTmpToken(tmpToken);
        if(isValidate){
            errorType       = ErrorType.VALID_TOKEN;
        }else{
            errorType       = ErrorType.INVALID_TOKEN;
        }
        return ResponseEntity.ok()
                .body(new Message(errorType, errorType.toString(), isValidate));
    }

    @Override
    public Optional<Member> findById(String Id){
        return memberRepository.findById(Id);
    }

    @Override
    public ResponseEntity<Object> verifyRecaptcha(Map<String, Object> recaptchaData){
        String token                        = (String) recaptchaData.get("token");
        HttpHeaders httpHeaders             = new HttpHeaders();
        RestTemplate restTemplate           = new RestTemplate();
        MultiValueMap<String, String> map   = new LinkedMultiValueMap<>();

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        map.add("secret",   recaptchaSecret);
        map.add("response", token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);

        Map<String,Object> response = restTemplate.postForObject(recaptchaServerURL, request, Map.class); // 해당 url로 token과 secret key를 전송. 유효성 검증.
        boolean bool = (boolean)response.get("success");

        return ResponseEntity.ok()
                .body(new Message(ErrorType.AUTHENTICATION_SUCCESS,"수신 완료", bool));
    }
}
