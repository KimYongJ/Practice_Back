package com.practice_back.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice_back.dto.LoginDTO;
import com.practice_back.repository.CartRepository;
import com.practice_back.response.ErrorType;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

import static com.practice_back.handler.HandlerFunc.handlerException;


public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final CartRepository cartRepository;
    @Setter
    AuthenticationManager authenticationManager;
    public CustomLoginFilter(TokenProvider tokenProvider, CartRepository cartRepository){
        this.tokenProvider = tokenProvider;
        this.cartRepository = cartRepository;
        super.setFilterProcessesUrl("/api/auth/login"); // 로그인시 전달될 커스텀 api를 정의 한다.
    }


    /*
    * [ attemptAuthentication 함수 ]
    * - 사용자가 POST 요청으로 로그인 정보를 전송할 때 이 메소드가 호출 된다.
    * - 스프링 시큐리티는 UsernamePasswordAuthenticationToken을 사용해 인증을 수행하며 해당 함수에서 AuthenticationManager를 통해 Authentication를 생성하면
    *   AuthenticationManager는 UserDetailsService을 구현한 클래스를 찾아 AuthenticationProvider를 사용해 loadUserByUsername 함수를 호출한다.
     *  이 때 AuthenticationManager는 loadUserByUsername함수 호출로 반환된 UserDetails 객체와 사용자가 입력한 비밀번호를 비교해 인증을 진행한다.
    * */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Map<String, Object> result;
        try {
            result = getBody(request); // 사용자 요청에서 이메일과 pw를 빼온다.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 사용자가 입력한 아이디와 비밀번호를 가져옴
        String email =(String) result.get("email");
        String password =(String) result.get("password");

        // UsernamePasswordAuthenticationToken 객체를 생성하고,
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        /*
        * 로그인 유저의 검증은 AuthenticationManager가 처리한다.
        * AuthenticationManager는 스프링 시큐리티의 필터에서 인증된 값을 정의하는 API로 SecurityContextHolder에 인증 값을 저장한다.
        * */
        return authenticationManager.authenticate(authRequest);// 로그인 유저의 검증은 AuthenticationManager가 처리한다.
    }

    /*
    * 로그인 성공시 호출되는 함수
    * */
    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication authentication) throws IOException{
        // 로그인에 성공한 유저
        UserDetails principal = (UserDetails)authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String email = principal.getUsername();

        String accessToken = tokenProvider.ceateAccessToken(email, authorities); // email을 통해 사용자의 권한을 가져와 accessToken을 생성
        tokenProvider.saveCookie(response,"accessToken",accessToken); // 응답에 토큰을 저장한다.
        long cntCartItems = cartRepository.countItemsByMemberEmail(email);
        boolean master = authorities.contains("ROLE_ADMIN");
        LoginDTO loginDTO = LoginDTO.builder()
                .cntCartItems(cntCartItems)
                .master(master)
                .build();
        handlerException(response, ErrorType.LOGIN_SUCCESS, loginDTO);
    }
    /*
    * 로그인 실패시 호출되는 함수
    * */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        handlerException(response, ErrorType.LOGIN_FAILED);
    }

    public Map<String, Object> getBody(HttpServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    sb.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }


        ObjectMapper mapper = new ObjectMapper(); 												// 문자열을 키, 벨류 형태로 바꾸기 위해 맵퍼 사용
        Map<String, Object> resultMap = mapper.readValue(sb.toString(), Map.class);			// 문자열 형태를 키:벨류 형태인 해시맵으로 변환한다.
        return resultMap;
    }
}