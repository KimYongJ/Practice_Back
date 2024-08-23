package com.practice_back.service.impl;
import com.practice_back.factory.FactoryUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.practice_back.entity.Member;
import com.practice_back.mockClass.MockOauth2UserInfo;
import com.practice_back.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OAuth2UserServiceImplTest {

    @SpyBean
    private OAuth2UserServiceImpl oauth2UserService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder   passwordEncoder;
    @Autowired
    private FactoryUserInfo factoryUserInfo;
    private OAuth2UserRequest userRequest;
    private OAuth2User oAuth2User;
    private String name = "korean";
    private String email = "korean@naver.com";
    private String picture = "dummy";
    private String userNameAttributeName = "sub";
    private Map<String, Object> attributes = new HashMap<>();
    @BeforeEach
    void setUp(){
        userRequest = mock(OAuth2UserRequest.class); // 요청 세팅
        oAuth2User = mock(OAuth2User.class); // OAuth2User 세팅

        attributes.put("name",      name);
        attributes.put("email",     email);
        attributes.put("picture",   picture);
        attributes.put("sub",       email);

        // loadUser 함수 모킹 설정
        doReturn(oAuth2User).when(oauth2UserService).loadUserFromSuper(userRequest);
        doReturn(userNameAttributeName).when(oauth2UserService).extractUserNameAttributeName(userRequest);
        doReturn("Google").when(oauth2UserService).getProvider(userRequest);
        doReturn(new MockOauth2UserInfo(attributes)).when(oauth2UserService).makeOauth2UserInfo(anyString(), any(OAuth2User.class));
    }
    @DisplayName("OAuth 요청이 전달되면 해당 데이터에서 아이디, 이메일, 사진을 뽑은 후 데이터 베이스에 저장한다.")
    @Test
    void loadUser(){
        // Given When
        OAuth2User result = oauth2UserService.loadUser(userRequest);
        List<Member> member = memberRepository.findAllByEmailIn(List.of(email));
        // Then
        assertThat(result).isNotNull()
                .satisfies(res->{
                    Map<String, Object> resAttributes = res.getAttributes();
                    assertTrue(attributes.equals(resAttributes));
                });
        assertThat(member).hasSize(1)
                .satisfies(res->{
                    Member mem = res.get(0);
                    assertEquals(mem.getEmail(),email);
                    assertEquals(mem.getPicture(),picture);
                });
    }

}