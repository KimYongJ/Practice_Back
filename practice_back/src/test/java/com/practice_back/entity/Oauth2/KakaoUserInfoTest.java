package com.practice_back.entity.Oauth2;

import com.practice_back.response.ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KakaoUserInfoTest {
    @DisplayName("OAuth 2.0 카카오 인증 객체 생성을 할 수 있다.")
    @Test
    void testKakaoUserInfo(){
        // Given
        Map<String, Object> properties = Map.of(
                "nickname","dummynick",
                "profile_image","dummyIMG"
        );
        Map<String, Object> kakao_account = Map.of(
                "email","kkk@gmail.com"
        );
        Map<String, Object> attributes = Map.of(
                "kakao_account",kakao_account,
                "properties",properties,
                "id","KKK"
        );
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority("ROLE_USER",attributes)),
                attributes,
                "id"
        );
        // When
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User);
        // Then
        assertEquals(kakaoUserInfo.getAttributes(), attributes);
        assertEquals(kakaoUserInfo.getName(), properties.get("nickname"));
        assertEquals(kakaoUserInfo.getEmail(), kakao_account.get("email"));
        assertEquals(kakaoUserInfo.getPicture(), properties.get("profile_image"));
        assertEquals(kakaoUserInfo.getProviderId(), attributes.get("id"));
        assertEquals(kakaoUserInfo.getProvider(), ProviderType.KAKAO.getValue());
    }
}