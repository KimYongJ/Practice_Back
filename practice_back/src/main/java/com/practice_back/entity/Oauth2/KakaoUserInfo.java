package com.practice_back.entity.Oauth2;

import com.practice_back.response.ProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo{
    private final Map<String, Object> attributes;
    private final Map<String, Object> kakao_account;
    private final Map<String, Object> properties;
    public KakaoUserInfo(OAuth2User oAuth2User){
        this.attributes = oAuth2User.getAttributes();
        kakao_account   = (Map<String, Object>)attributes.get("kakao_account");
        properties      = (Map<String, Object>)attributes.get("properties");
    }
    @Override
    public Map<String, Object> getAttributes(){
        return attributes;
    }

    @Override
    public String getName(){
        return (String) properties.get("nickname");
    }
    @Override
    public String getEmail(){
        return (String) kakao_account.get("email");
    }
    @Override
    public String getPicture(){
        return (String) properties.get("profile_image");
    }
    @Override
    public String getProviderId(){ return String.valueOf(attributes.get("id"));}
    @Override
    public String getProvider(){return ProviderType.KAKAO.getValue();}
}
