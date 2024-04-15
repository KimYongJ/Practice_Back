package com.practice_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;


    public static String getNaverEmail( Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return (String) response.get("email");
    }
    // 카카오 이메일은 가져오기 위해 별도 승인이 필요해  nickname으로 임시 개발
    public static String getKaKaoEmail( Map<String, Object> attributes){
        Map<String, Object> kakao_account = (Map<String, Object>)attributes.get("kakao_account");
        return (String) kakao_account.get("email");
    }
    public static String getGoogleEmail(Map<String, Object> attributes){
        return (String)attributes.get("email");
    }
    public static String getGithubEmail(Map<String, Object> attributes) {
        return (String)attributes.get("email");
    }
    public static OAuthAttributes kakaoMemberInfo(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");
        Map<String, Object> kakao_account = (Map<String, Object>)attributes.get("kakao_account");
        return OAuthAttributes.builder()
                .name((String) properties.get("nickname"))
                .email((String) kakao_account.get("email"))
                .picture((String) properties.get("profile_image"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public static OAuthAttributes naverMemberInfo(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public static OAuthAttributes googleMemberInfo(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes githubMemberInfo(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


}
