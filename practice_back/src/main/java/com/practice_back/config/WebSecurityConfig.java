package com.practice_back.config;
import com.practice_back.jwt.AccessTokenFilter;
import com.practice_back.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * [ 스프링 시큐리티 흐름 ]
 * 1. 요청 가로채기 : 스프링 시큐리티는 http 요청을 가로채 보안 처리를 수행. 이는 필터 체인을 통해 수행됨.
 *                  적절한 인증 메커니즘을 통해 AuthenticationManager를 활성화하고 개발자가 AuthenticationManager에 UsernamePasswordAuthenticationToken을 전달한다.
 *                  AuthenticationManager에 개발자가 인증 정보를 전달하면 내부적으로 AuthenticationProvider를 사용해 실제 인증 처리함. 이 과정에서 AuthenticationProvider는 loadUserByUsername 함수를 호출.
 * 1-1. AuthenticationManager 데이터 전달 코드 예시 : Authentication authentication = authenticationManager.authenticate(      new UsernamePasswordAuthenticationToken(empId, password)  );
 *                                               : Authentication authentication = authenticationManagerBuilder.getObject().authenticate( new UsernamePasswordAuthenticationToken(empId, password)  );
 * 2. 인증 및 권한 부여 : 요청에 대해 인증(Authentication) 및 권한 부여(Authorization) 절차를 진행. 이 과정에서 사용자 신원 확인과 해당 요청에 대한 접근 권한 검증
 * 3. SecurityFilterChain 은 여러 보안 필터들을 순차적으로 적용하는 체인으로 필터 순서와 종류를 조정할 수 있다.
 * */


/*
* [ @RequiredArgsConstructor ]
* 클래스 내에 final 키워드가 붙거나 @NonNull 어노테이션이 붙은 필드들을 인자로 하는 생성자를 자동으로 생성
* */
@RequiredArgsConstructor
/*
 * [ @Configuration 설명 ]
 * - 해당 어노테이션을 사용하면 클래스가 스프링의 설정 정보를 포함하고있음을 나타낸다.(스프링 컨테이너에 의해 빈 설정을 위한 것으로 간주됨)
 * - 해당 어노테이션을 쓴 클래스는 자동으로 빈으로 등록됨.(@Component 어노테이션 사용 불필요)
 * - 이 클래스 안에 정의된 함수 중 @Bean 어노테이션이 붙은 메소드들로 부터 반환되는 객체들은 스프링 빈으로 등록된다. ( 함수 반환 객체가 빈으로 등록됨에 유의 )
 * */
@Configuration
/*
 * [ @EnableWebSecurity 설명 ]
 * - 이 어노테이션은 Spring Security 설정을 활성화한다. 이 어노테이션이 없으면 스프링 시큐리티의 사용자 정의 보안 구성을 적용할 수 없다.
 * - SecurityConfigurerAdapter를 상속받아 보안 설정을 커스텀할 수 있게 하며 어느 클래스이던 SecurityConfigurerAdapter를 상속받아 구현만 하면 된다.
 * - 어플리케이션에서 이 어노테이션은 한번만 써야한다. 여러번 쓰면 스프링이 어떤 것을 우선으로 할지 결정하지 못하므로 한번만 쓰는것을 권장 한다.
 * */
@EnableWebSecurity
public class WebSecurityConfig { //extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;

    /*
     * [ configure 함수 설명 ]
     * - 해당 함수는  HTTP 요청에 대한 보안 요구 사항(인증 방식, 접근 권한, CSRF 보호 등)을 사용자가 원하는 대로 설정하고 특정 필터를 보안 필터 체인에 추가하는 역할을 한다.
     * - 해당 함수의 실행순서 : 애플리케이션 시작시 스프링 시큐리티는 초기화 되는데 이 과정에서 SecurityConfigurerAdapter클래스를 상속받은 클래스의 configure 함수가 호출된다.
     *                       configure 함수에 정의된 설정은 스프링 시큐리티의 보안 필터체인에 적용된다. 이 필터 체인은 들어오는 HTTP 요청을 처리하며, 각 요청에 대해 설정된 커스텀 보안 정책을 적용함.
     *
     * @Override
     * public void configure(HttpSecurity http) {
     *    AccessTokenFilter customFilter = new AccessTokenFilter(tokenProvider,tokenDataRepository);
     *    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
     * }
     */



    /*
     * [ passwordEncoder 빈등록 설명 ]
     * - passwordEncoder() 함수를 빈으로 등록해놓고 사용한다.(가독성을 위해서..)
     * - 사용자 회원가입시 비밀번호를 그냥 저장하지 않고 해당 인코더를 사용해 암호화 한 후 저장한다.(DB에는 암호화된 데이터가 저장됨)
     * - 사용 예시 : passwordEncoder.encode(password) // 반환된 문자열을 그대로 DB에 저장
     * - BCryptPasswordEncoder : 복호화가 불가능한 인코딩 방법으로 BCrypt 해싱 알고리즘을 사용. 해시 속도를 조절할 수 있으며 브루트 포스 공격에 강하고 해시 결과가 각각 다른 솔트(salt)를 사용하기 때문에 동일한 비밀번호라도 해시 값이 다름
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     *   [ filterChain 함수 ]
     *   - filterChain 함수는 애플리케이션이 시작될 때 한 번 호출된다. HttpSecurity객체 안에 보안 구성을 정의하고 HttpSecurity객체를 빌드하여 SecurityFilterChain 객체를 만들어 반환해 SecurityFilterChain객체를 빈으로 등록한다.
     *   - 스프링 시큐리티는 SecurityFilterChain 객체를 사용해 모든 사용자 요청을 정의된 규칙에 따라 처리한다.
     *  [ HttpSecurity 객체 설명 ]
     *   - 스프링 시큐리티 구성에 사용되며 다음과 같은 보안 설정을 정의하는데 사용
     *   - 경로별 접근 제어: 특정 URL 패턴에 대한 접근 권한을 설정.
     *   - 인증 메커니즘: 폼 로그인, HTTP Basic 인증, OAuth2 등 다양한 인증 방식을 설정.
     *   - 로그아웃 처리: 로그아웃 시의 동작을 정의.
     *   - 세션 관리: 세션 생성 정책, 세션 고정 보호, 최대 세션 수 등을 설정.
     *   - CSRF 보호: 사이트 간 요청 위조(Cross-Site Request Forgery) 보호 활성화 유무 설정
     *   - CORS: Cross-Origin Resource Sharing 관련 설정.
     *   - 기타 보안 관련 설정: 헤더 보안, 리다이렉트 정책 등을 설정합니다.
     *   [ addFilterBefore 함수 ]
     *   - 이 함수는 스프링 시큐리티의 필터 체인에 사용자 정의 필터를 추가하는 역할을 한다. addFilterBefore와 addFilterAfter 함수가 있으며 개발자가 정의한 처리를 필터 체인에 통합할 때 사용된다.
     *     해당 함수를 여러번 사용해 필터 체인에 여러개의 필터를 추가할 수도 있다.
     *     addFilterBefore(A, B): 필터 A를 필터 B '전에' 추가함.(B는 이미 필터체인에 존재하는 필터여야만한다 없으면 오류)
     *     addFilterBefore(B, C): 필터 B를 필터 C '전에' 추가함.(C는 이미 필터체인에 존재하는 필터여야만한다 없으면 오류)
     *   [ UsernamePasswordAuthenticationFilter.class ]
     *   - 해당 클래스는 스프링 시큐리티에서 사용자의 로그인(인증) 요청을 처리하는 표준필터이다.
     *     addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class) 이렇게 코드를 작성하면,
     *     customFilter를 UsernamePasswordAuthenticationFilter 전에 실행하도록 지정하는 것이다.
     *     (이때, 필터 체인에 이미 UsernamePasswordAuthenticationFilter가 들어 있다.)
     * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpsecurity) throws Exception { // 이 메소드는 스프링 시큐리트의 핵심 구성 요소 중 하나이다. 이 메소드는 스프링 부트의 자동 구성과정 중 스프링 시큐리티에 의해 호출됨
        httpsecurity
                .authorizeRequests()
                .antMatchers("/api/user/**").permitAll()
//                .antMatchers("/api/user/category").permitAll()
//                .antMatchers("/api/user/items/**").permitAll()
//                .antMatchers("/api/user/number").permitAll()
                // "/api/user/**" 경로는 "ADMIN", "USER", "MANAGER" 역할을 가진 사용자만 접근 가능 //hasAnyRole, hasRole은 ROLE_ 접두사를 붙임
                //  .antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER", "MANAGER")
                //  .antMatchers("/api/admin/**").hasRole("ADMIN")
                // 그 외 모든 요청은 인증이 필요함
                .anyRequest().authenticated()
                .and()
                .csrf().disable() // CSRF 보호를 비활성화
                // REST API를 통해 세션 없이 토큰을 주고받으며 데이터를 주고받기 때문에 세션설정은 STATELESS로 설정.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling();
                 //이후 예외를 핸들링하는 것은 예시
                 // .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                 // .accessDeniedHandler(jwtAccessDeniedHandler);

        // JWT 필터 추가
        AccessTokenFilter accesstokenfilter = new AccessTokenFilter(tokenProvider);
        httpsecurity.addFilterBefore(accesstokenfilter, UsernamePasswordAuthenticationFilter.class);

        return httpsecurity.build();
    }
}
