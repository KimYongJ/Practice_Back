package com.practice_back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class OAuthAttributesTest {
    @DisplayName("빌더를 통해 OAuthAttributes 객체를 생성할 수 있다.")
    @Test
    void testOAuthAttributes(){
        // Given
        String nameAttributeKey = "NAVER";
        String name = "KANG";
        String email = "kkk@naver.com";
        String picture = "IMG";
        Map<String, Object> attributes = Map.of(
                nameAttributeKey,nameAttributeKey,
                name,name,
                email,email,
                picture,picture
        );

        // When
        OAuthAttributes oAuthAttributes = OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .name(name)
                .email(email)
                .picture(picture)
                .build();

        // Then
        assertEquals(oAuthAttributes.getNameAttributeKey(), nameAttributeKey);
        assertEquals(oAuthAttributes.getName(), name);
        assertEquals(oAuthAttributes.getEmail(), email);
        assertEquals(oAuthAttributes.getPicture(), picture);
        assertThat(oAuthAttributes.getAttributes())
                .satisfies(res->{
                    assertEquals(res.get(nameAttributeKey),nameAttributeKey);
                    assertEquals(res.get(name),name);
                    assertEquals(res.get(email),email);
                    assertEquals(res.get(picture),picture);
                });
    }

    @DisplayName("attributes안에 있는 네이버 email을 가져올 수 있다.")
    @Test
    void getNaverEmail(){
        // Given
        String email = "kkk@naver.com";
        Map<String, Object> response = Map.of("email",email);
        Map<String, Object> attributes = Map.of("response", response);

        // When
        String resEmail = OAuthAttributes.getNaverEmail(attributes);

        // Then
        assertEquals(email, resEmail);
    }
    @DisplayName("attributes안에 있는 카카오 email을 가져올 수 있다.")
    @Test
    void getKaKaoEmail(){
        // Given
        String email = "kkk@naver.com";
        Map<String, Object> kakao_account = Map.of("email",email);
        Map<String, Object> attributes = Map.of("kakao_account", kakao_account);

        // When
        String resEmail = OAuthAttributes.getKaKaoEmail(attributes);

        // Then
        assertEquals(email, resEmail);
    }
    @DisplayName("attributes안에 있는 구글 email을 가져올 수 있다.")
    @Test
    void getGoogleEmail(){
        // Given
        String email = "kkk@naver.com";
        Map<String, Object> attributes = Map.of("email", email);

        // When
        String resEmail = OAuthAttributes.getGoogleEmail(attributes);

        // Then
        assertEquals(email, resEmail);
    }
    @DisplayName("attributes안에 있는 깃허브 email을 가져올 수 있다.")
    @Test
    void getGithubEmail(){
        // Given
        String email = "kkk@naver.com";
        Map<String, Object> attributes = Map.of("email", email);

        // When
        String resEmail = OAuthAttributes.getGithubEmail(attributes);

        // Then
        assertEquals(email, resEmail);
    }
    @DisplayName("카카오에서 받은 데이터를 통해 OAuthAttributes객체를 생성할 수 있다.")
    @Test
    void kakaoMemberInfo(){
        // Given
        String userNameAttributeName = "KEY";
        String nickname = "nickname";
        String email = "email";
        String profile_image = "profile_image";

        Map<String, Object> properties = Map.of(
                nickname,nickname,
                profile_image,profile_image
        );

        Map<String, Object> kakao_account = Map.of(email,email);

        Map<String, Object> attributes = Map.of(
                "properties", properties,
                "kakao_account",kakao_account
        );

        // When
        OAuthAttributes oAuthAttributes = OAuthAttributes.kakaoMemberInfo(userNameAttributeName,attributes);

        // Then
        assertEquals(oAuthAttributes.getName(), nickname);
        assertEquals(oAuthAttributes.getEmail(), email);
        assertEquals(oAuthAttributes.getPicture(), profile_image);
        assertEquals(oAuthAttributes.getAttributes(), attributes);
        assertEquals(oAuthAttributes.getNameAttributeKey(), userNameAttributeName);
    }
    @DisplayName("네이버에서 받은 데이터를 통해 OAuthAttributes객체를 생성할 수 있다.")
    @Test
    void naverMemberInfo(){
        // Given
        String userNameAttributeName = "KEY";
        String name = "name";
        String email = "email";
        String profile_image = "profile_image";

        Map<String, Object> response = Map.of(
                name,name,
                email,email,
                profile_image,profile_image
        );

        Map<String, Object> attributes = Map.of("response",response);

        // When
        OAuthAttributes oAuthAttributes = OAuthAttributes.naverMemberInfo(userNameAttributeName,attributes);

        // Then
        assertEquals(oAuthAttributes.getName(), name);
        assertEquals(oAuthAttributes.getEmail(), email);
        assertEquals(oAuthAttributes.getPicture(), profile_image);
        assertEquals(oAuthAttributes.getAttributes(), attributes);
        assertEquals(oAuthAttributes.getNameAttributeKey(), userNameAttributeName);
    }
    @DisplayName("구글에서 받은 데이터를 통해 OAuthAttributes객체를 생성할 수 있다.")
    @Test
    void googleMemberInfo(){
        // Given
        String userNameAttributeName = "KEY";
        String name     = "name";
        String email    = "email";
        String picture  = "picture";

        Map<String, Object> attributes = Map.of(
                name,name,
                email,email,
                picture,picture
        );

        // When
        OAuthAttributes oAuthAttributes = OAuthAttributes.googleMemberInfo(userNameAttributeName,attributes);

        // Then
        assertEquals(oAuthAttributes.getName(), name);
        assertEquals(oAuthAttributes.getEmail(), email);
        assertEquals(oAuthAttributes.getPicture(), picture);
        assertEquals(oAuthAttributes.getAttributes(), attributes);
        assertEquals(oAuthAttributes.getNameAttributeKey(), userNameAttributeName);
    }
    @DisplayName("깃허브에서 받은 데이터를 통해 OAuthAttributes객체를 생성할 수 있다.")
    @Test
    void githubMemberInfo(){
        // Given
        String userNameAttributeName = "KEY";
        String name     = "name";
        String email    = "email";
        String avatar_url  = "avatar_url";

        Map<String, Object> attributes = Map.of(
                name,name,
                email,email,
                avatar_url,avatar_url
        );

        // When
        OAuthAttributes oAuthAttributes = OAuthAttributes.githubMemberInfo(userNameAttributeName,attributes);

        // Then
        assertEquals(oAuthAttributes.getName(), name);
        assertEquals(oAuthAttributes.getEmail(), email);
        assertEquals(oAuthAttributes.getPicture(), avatar_url);
        assertEquals(oAuthAttributes.getAttributes(), attributes);
        assertEquals(oAuthAttributes.getNameAttributeKey(), userNameAttributeName);
    }
}