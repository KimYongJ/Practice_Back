package com.practice_back.jwt;

import com.practice_back.response.ErrorType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.practice_back.handler.HandlerFunc.handlerException;


@Component
public class TokenProvider {

    /*
    * @Value어노테이션을 쓴 클래스는 스프링 빈으로 등록될 때 yml 파일의 설정 값을 해당 필드에 주입받은 후 생성된다.
    */
    @Value("${token.authoritiesKey}")
    private String AUTHORITIES_KEY;
    @Value("${token.bearerType}")
    private String BEARER_TYPE; // Bearer는 OAuth 2.0  표준에서 정의된 인증 스키마이다.
    @Value("${token.accessTokenExpireTime}")
    private Integer ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${token.tempTokenExpireTime}")
    private Integer TEMP_TOKEN_EXPIRE_TIME;

    /*
    * Key 객체는 JWT 토큰 생성에 사용되며 Key객체를 사용함으로 JWT가 변조되지 않았고, 해당 서버에서만 생성되었음을 보증한다.
     * */
    private final Key key;

    TokenProvider(@Value("${token.secret}") String key)
    {
        /*
        *  [ Decoders.BASE64.decode(key) 설명 ]
        *  - 주어진 key값을 BASE64로 디코딩하여 바이트 배열로 변환한다.
        * */
        byte[] keyBytes = Decoders.BASE64.decode(key);

        /*
        *  [ Keys.hmacShaKeyFor(keyBytes) ]
        *  - 주어진 바이트배열(keyBytes)을 사용해 HMAC(Hash-based Message Authentication Code) SHA(보안 해시 알고리즘) 키를 생성하는 것
        *  - HMAC은 메시지 인증을 위한 암호화 기법으로, SHA는 이러한 HMAC을 계산하기 위한 해시 알고리즘을 의미
        * */
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 엑세스 토큰 생성
    public String createAccessToken(String email, String auth)
    {
        /*
        * 토큰 생성시 토큰에 세션 정보도 포함가능하다(클레임사용) 아래 코드 예시
        * HttpServletRequest request // String sessionId = request.getSession().getId();
        * .claim("SESSION_ID", sessionId) // 세션 ID를 토큰에 포함 코드
        * */
        return Jwts.builder()
                .setSubject(email)                                                          // 토큰의 "sub"(subject) 클레임을 설정한다. sub 클레임은 토큰의 주체를 식별하는 데 사용되며, 일반적으로 사용자 ID나 이메일 주소와 같은 고유한 식별자를 사용함
                .claim(AUTHORITIES_KEY , auth)                                              // claim(String, Object): 사용자 정의 클레임을 설정. 이 메소드는 키-값 쌍으로 클레임을 추가하며, 여기서는 사용자의 권한을 나타내는 auth 값을 AUTHORITIES_KEY라는 키와 함께 저장
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // 토큰의 만료 시간을 설정. 현재 시간에서 ACCESS_TOKEN_EXPIRE_TIME(밀리초 단위)을 더하여 만료 시간을 지정
                .signWith(key, SignatureAlgorithm.HS512)                                    // JWT에 서명을 추가합니다. 서명은 토큰의 무결성과 인증을 보장하는 데 사용된다.. 여기서는 HS512 알고리즘과 사전에 정의된 key를 사용
                .compact();                                                                 // 위의 모든 설정으로 토큰을 생성하고, 최종적으로 문자열 형태로 압축하여 반환
    }
    // temp 토큰 생성
    public String createTempToken(String email)
    {
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY , "ROLE_USER")
                .setExpiration(new Date((new Date()).getTime() + TEMP_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰을 쿠키에 저장할 수 있도록 응답에 쿠키 저장
    public void saveCookie(HttpServletResponse response , String cookieName, String token, int flag){
        int MAX_AGE = ACCESS_TOKEN_EXPIRE_TIME;
        if(flag == 2){
            MAX_AGE = TEMP_TOKEN_EXPIRE_TIME;
        }
        ResponseCookie cookie =ResponseCookie.from(cookieName, token)
                .domain("localhost")
                .sameSite("Lax") // Lax, None, Strict
       //         .secure(true)//메서드를 호출하여 쿠키가 HTTPS를 통해서만 전송되도록 함. 개발환경에서는 비활성화, 비활성화 하면서 httpOnly가 true이면 sameSite는 None으로할 수 없다.
                .httpOnly(true)// JavaScript가 쿠키에 접근하지 못하도록 설정,XSS 공격으로부터 쿠키를 보호
                .path("/")
                .maxAge(MAX_AGE/1000)// 쿠키의 유효 시간 설정
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // Access 토큰을 검증하기 위한 함수
    public boolean validateToken(HttpServletResponse response, String token) throws Exception {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            handlerException(response, ErrorType.EXPIRED_TOKEN);
        } catch (Exception e) {
            handlerException(response, ErrorType.BAD_REQUEST);
        }
        return false;
    }
    // Temp 토큰을 검증하기 위한 함수
    public boolean validateTmpToken(String token){
        boolean bool = true;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        }catch (Exception e) {
            bool = false;
        }
        return bool;
    }


    // Jwt 토큰에서 인증 정보 조회, SecurityContextHolder에 저장하기 전 데이터를 가공하는 것
    public Authentication getAuthentication(HttpServletRequest request, String accessToken) {

        Claims claims = parseClaims(accessToken);// JWT 토큰에서 클레임(claims) 추출

        if (claims.get(AUTHORITIES_KEY) == null) {
            return null; // 권한이 없을 때 null 반환
        }

        // 권한 정보를 기반으로 GrantedAuthority 객체 리스트 생성
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)// 각 권한 문자열을 SimpleGrantedAuthority 객체로 변환
                        .collect(Collectors.toList());

        // UserDetails 객체 생성. 이 객체는 사용자의 email, 비밀번호(여기서는 비어 있음), 권한 목록을 포함
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        // 최종적으로 UsernamePasswordAuthenticationToken 반환
        // 이 토큰은 SecurityContextHolder에 설정되어 인증과 권한 부여에 사용됨
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Jwt토큰(엑세스토큰)을 claims 형태로 만드는 함수.
    public Claims parseClaims(String tokenName)
    {
        try{
            // JwtParserBuilder 인스턴스 생성 및 서명 키 설정
            JwtParser parser = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build();

            // 전달된 토큰 이름을 파싱하여 Claims 객체 반환, getBody 함수로 Claims 부분을 추출
            return parser.parseClaimsJws(tokenName).getBody();
        }catch(ExpiredJwtException e){
            return e.getClaims();// 토큰이 만료된 경우, 만료된 토큰의 Claims 반환
        }
    }

    // 사용자의 요청에서 엑세스 토큰을 꺼내오는 함수
    public String getToken(HttpServletRequest request, String tokenName){
        Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(tokenName.equals(cookie.getName())){
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }

    // 쿠키 리셋
    public void cookieReset(HttpServletResponse response, String tokenName)
    {
        ResponseCookie cookie =ResponseCookie.from(tokenName, "1")
                .domain("localhost")
                .sameSite("Lax") // Lax, None, Strict
                //         .secure(true)//메서드를 호출하여 쿠키가 HTTPS를 통해서만 전송되도록 함. 개발환경에서는 비활성화, 비활성화 하면서 httpOnly가 true이면 sameSite는 None으로할 수 없다.
                .httpOnly(true)// JavaScript가 쿠키에 접근하지 못하도록 설정,XSS 공격으로부터 쿠키를 보호
                .path("/")
                .maxAge(0)// 쿠키의 유효 시간 설정
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // 요청한 사용자의 정보를 꺼내오는 함수
    public static String getCurrentMemberInfo()
    {
        String memberInfo = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getName() != null)
        {
            memberInfo = authentication.getName();
        }
        return memberInfo;
    }

    // 컨텍스트 리셋(요청 사용자 정보 초기화)
    public static void contextReset()
    {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

}
