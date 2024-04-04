package com.practice_back.service.impl;


import com.practice_back.dto.OAuthAttributes;
import com.practice_back.entity.Authority;
import com.practice_back.entity.Cart;
import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import com.practice_back.service.AuthService;
import com.practice_back.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

/**
 * 인증 제공자(예 :google, naver..)로 부터 토큰을 정상 수령하여 인증이 완료되면 OAuth2UserService의 loadUser함수를 호출합니다.
 * loadUser 함수를 커스터마이징하여 인증된 정보를 통해 권한지정 및 회원 가입을 진행합니다.
 * */
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final PasswordEncoder   passwordEncoder;
    private final MemberRepository  memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User           = delegate.loadUser(userRequest);
        String registrationId           = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName    = userRequest.getClientRegistration().getProviderDetails()
                                                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes      = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        String email                    = attributes.getEmail();
        // email을 통해 멤버를 찾으며 없을 경우 member 데이터 신규 저장
        if( !memberRepository.existsByEmail(email) ){
            insertNewMember(email);
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROL_USER");
        return new DefaultOAuth2User(
                Collections.singleton(authority),
                oAuth2User.getAttributes(),
                userNameAttributeName);
    }

    public Member insertNewMember(String email){
        String randomPassword   = UUID.randomUUID().toString();
        Member member           = Member.builder()
                                        .email(email)
                                        .password(passwordEncoder.encode(randomPassword))
                                        .authority(Authority.ROLE_USER)
                                        .cart(new Cart())
                                        .build();
        member.getCart()
                .setMember(member);
        return memberRepository.save( member );
    }
}
