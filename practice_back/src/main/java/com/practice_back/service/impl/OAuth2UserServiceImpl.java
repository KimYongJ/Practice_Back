package com.practice_back.service.impl;


import com.practice_back.entity.Authority;
import com.practice_back.entity.Cart;
import com.practice_back.entity.Member;
import com.practice_back.entity.Oauth2.Oauth2UserInfo;
import com.practice_back.repository.MemberRepository;
import com.practice_back.service.impl.FactoryUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * 주석 1
 * */
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService{ /** 주석 2*/
    private final PasswordEncoder   passwordEncoder;
    private final MemberRepository  memberRepository;
    private final FactoryUserInfo factoryUserInfo;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User           = super.loadUser(userRequest);
        Oauth2UserInfo oauth2UserInfo   = factoryUserInfo.makeOauth2Userinfo(userRequest.getClientRegistration().getRegistrationId(),oAuth2User);
        String userNameAttributeName    = userRequest.getClientRegistration().getProviderDetails()
                                            .getUserInfoEndpoint().getUserNameAttributeName();

        insertMember(oauth2UserInfo);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROL_USER");
        return new DefaultOAuth2User(
                Collections.singleton(authority),
                oauth2UserInfo.getAttributes(),
                userNameAttributeName);
    }

    public void insertMember(Oauth2UserInfo oauth2UserInfo){
        String email    = oauth2UserInfo.getEmail();
        String picture  = oauth2UserInfo.getPicture();
        String Id       = oauth2UserInfo.getProviderId().concat( oauth2UserInfo.getProvider() );
        Optional<Member> memberOptional = memberRepository.findById(Id);
        Member member = null;
        if(memberOptional.isPresent()){
            member = memberOptional.get();
            member.changePicture(picture);
        }else{
            member = Member.builder()
                    .id(Id)
                    .email(email)
                    .password(passwordEncoder.encode( UUID.randomUUID().toString() ))
                    .picture(picture)
                    .authority(Authority.ROLE_USER)
                    .cart(new Cart())
                    .build();
            member.getCart()
                    .setMember(member);
        }
        memberRepository.save( member );

    }
}


/**
 * 주석 1
 * 인증 제공자(예 :google, naver..)로 부터 토큰을 정상 수령하여 인증이 완료되면 OAuth2UserService의 loadUser함수를 호출합니다.
 * loadUser 함수를 커스터마이징하여 인증된 정보를 통해 권한지정 및 회원 가입을 진행합니다.
 *
 * 주석 2
 * DefaultOAuth2UserService 클래스는  OAuth2UserService<OAuth2UserRequest, OAuth2User> 인터페이스를 구현한 구현체입니다.
 * */