package com.practice_back.entity.Oauth2;

import com.practice_back.response.ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoogleUserInfoTest {
    @DisplayName("OAuth 2.0 구글 인증 객체 생성을 할 수 있다.")
    @Test
    void testGoogleUserInfo(){
        // Given
        Map<String, Object> attributes = Map.of(
                "name", "이름",
                "email", "kkk@gmail.com",
                "picture", "IMG",
                "sub","subtext",
                "id","dummyID"
        );
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority("ROLE_USER",attributes)),
                attributes,
                "id"
        );

        // When
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oAuth2User);

        // Then
        assertEquals(googleUserInfo.getAttributes(), attributes);
        assertEquals(googleUserInfo.getName(), attributes.get("name"));
        assertEquals(googleUserInfo.getEmail(), attributes.get("email"));
        assertEquals(googleUserInfo.getPicture(), attributes.get("picture"));
        assertEquals(googleUserInfo.getProviderId(), attributes.get("sub"));
        assertEquals(googleUserInfo.getProvider(), ProviderType.GOOGLE.getValue());
    }
}