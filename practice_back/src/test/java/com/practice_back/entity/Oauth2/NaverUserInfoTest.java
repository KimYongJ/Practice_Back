package com.practice_back.entity.Oauth2;

import com.practice_back.response.ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NaverUserInfoTest {
    @DisplayName("OAuth 2.0 네이버 인증 객체 생성을 할 수 있다.")
    @Test
    void testNaverUserInfo(){
        // Given
        Map<String, Object> responseMap = Map.of(
                "id","KKK",
                "name","kim",
                "email","kkk@gmail.com",
                "profile_image","dummyIMG"
        );
        Map<String, Object> attributesMap = Map.of(
                "response", responseMap,
                "id","KKK"
        );

        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority("ROLE_USER", attributesMap)),
                attributesMap,
                "id"
        );

        // When
        NaverUserInfo naverUserInfo = new NaverUserInfo(oAuth2User);

        // Then
        assertEquals(responseMap.get("id"), naverUserInfo.getProviderId());
        assertEquals(responseMap.get("name"), naverUserInfo.getName());
        assertEquals(responseMap.get("email"), naverUserInfo.getEmail());
        assertEquals(responseMap.get("profile_image"), naverUserInfo.getPicture());
        assertEquals(ProviderType.NAVER.getValue(), naverUserInfo.getProvider());
    }
}