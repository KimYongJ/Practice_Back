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
class GithubUserInfoTest {
    @DisplayName("OAuth 2.0 깃허브 인증 객체 생성을 할 수 있다.")
    @Test
    void testGithubUserInfo(){
        // Given
        Map<String, Object> attributes = Map.of(
                "name","NAME",
                "email","kkk@gmail.com",
                "avatar_url","IMGURL",
                "id","GITHUB"
        );
        OAuth2User oauth2User = new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority("ROLE_USER",attributes)),
                attributes,
                "id"
        );

        // When
        GithubUserInfo githubUserInfo = new GithubUserInfo(oauth2User);

        // Then
        assertEquals(githubUserInfo.getAttributes(),    attributes);
        assertEquals(githubUserInfo.getName(),          attributes.get("name"));
        assertEquals(githubUserInfo.getEmail(),         attributes.get("email"));
        assertEquals(githubUserInfo.getPicture(),       attributes.get("avatar_url"));
        assertEquals(githubUserInfo.getProviderId(),    attributes.get("id"));
        assertEquals(githubUserInfo.getProvider(),      ProviderType.GITHUB.getValue());
    }
}