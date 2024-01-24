package com.practice_back.service.impl;

import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 클래스 내에 final 키워드가 붙거나 @NonNull 어노테이션이 붙은 필드들을 인자로 하는 생성자를 자동으로 생성
/*
 * [ UserDetailsService 구현 이유 ]
 * - 스프링 시큐리티는 사용자의 인증 정보를 조회하는 로직을 UserDetailsService 인터페이스를 통해 제공함.
 * - 개발자는 이 인터페이스를 구현해 데이터베이스에서 사용자 정보를 조회하는 로직을 작성해야 한다.
 * */
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberrepository;

    /*
     * [ loadUserByUsername 함수 ]
     * - 이 함수는 데이터베이스에서 사용자 정보를 조회한 후 이를 UserDetails 객체로 반환한다. 반환된 UserDetails 객체에는 id,pw,권한 등이 포함된다.
     * - 호출 순서 : AuthenticationManager는 UserDetailsService을 구현한 클래스를 찾아 AuthenticationProvider를 사용해 loadUserByUsername 함수를 호출한다.
     *              이 때 AuthenticationManager는 loadUserByUsername함수 호출로 반환된 UserDetails 객체와 사용자가 입력한 비밀번호를 비교해 인증을 진행한다.
     * */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberrepository.findByEmail(email);
        // 데이터베이스에서 사용자 정보 조회
        // 사용자 정보를 UserDetails 객체로 반환
        return member
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + " 을 DB에서 찾을 수 없습니다"));
    }

    /*
    * [ createUserDetails 함수 ]
    * - 인자로 전달된 객체에서 ID, PW, 권한을 뽑아내 UserDetails객체로 만들어 반환한다.
    * - GrantedAuthority : 스프링 시큐리티에서 권한을 나타내는 인터페이스이며 이 인터페이스의 구현체는 사용자 권한을 나타내고, 스프링 시큐리티는 이 권한 정보를 사용해 권한에 기반한 접근 제어를 수행한다.
    *                       SimpleGrantedAuthority객체를 통해 권한을 생성할 때 권한 문자열 규약을 따라야 한다. 규약을 따라야 스프링 시큐리티가 권한을 관리하고 처리할 수 있다.
    *                       권한의 ★문자열은 대문자를 사용해야 하고, ★접두사는 "ROLE_" 사용.( "ROLE_ADMIN", "ROLE_USER","ROLE_MANAGER", "ROLE_EDITOR" )
    *                       ( 스프링 시큐리티 버전이나 구성에 따라 권한 문자열에 대한 처리가 다를 수 있으므로 사용 버전 공식문서 참조 필수 )
    *                       SimpleGrantedAuthority 객체에 "ROLE_ADMIN", "ROLE_USER"와 같은 문자열 전달시 필터 체인 설정에서도 동일 문자열을 사용해야 한다.
    *                       ( 필터 체인에서 함수에 따라 "ROLE_" 접두사를 자동 추가해주는 것도있음. ex)hasRole("ADMIN")은 실제로 "ROLE_ADMIN"을 확인한다.hasAuthority는 전달된 문자열을 그대로 사용 )
    * - User 객체 : User 클래스는 스프링 시큐리티에서 제공하는 UserDetails 인터페이스의 표준 구현체이다. 이 클래스 생성자는 ID, PW, 그리고 권한 목록을 인자로 받는다.
    *               User객체 생성할 때 권한 셋팅시 Collection<GrantedAuthority> 타입이 필요하다. 단일 권한을 가진 사용자의 경우 Collections.singleton을 사용해 간단히 구현 가능

- 스프링 시큐리티에서 hasRole과 hasAuthority는 약간 다르게 동작합니다. hasRole 메소드는 역할 이름 앞에 자동으로 "ROLE_" 접두사를 추가합니다. 예를 들어, hasRole("ADMIN")은 실제로 "ROLE_ADMIN"을 확인합니다. 반면, hasAuthority는 전달된 문자열을 그대로 사용합니다.
    * */
    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority());
        return new User(
                String.valueOf(member.getEmail()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
