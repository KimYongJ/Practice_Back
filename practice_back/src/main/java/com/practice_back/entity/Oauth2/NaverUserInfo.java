package com.practice_back.entity.Oauth2;

import com.practice_back.response.ProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUserInfo implements Oauth2UserInfo{
    private final Map<String, Object> attributes;
    private final Map<String, Object> response;
    public NaverUserInfo(OAuth2User oAuth2User){
        this.attributes = oAuth2User.getAttributes();
        this.response   = (Map<String, Object>)attributes.get("response");
    }
    @Override
    public Map<String, Object> getAttributes(){
        return attributes;
    }

    @Override
    public String getName(){
        return (String) response.get("name");
    }
    @Override
    public String getEmail(){
        return (String) response.get("email");
    }
    @Override
    public String getPicture(){
        return (String) response.get("profile_image");
    }
    @Override
    public String getProviderId(){ return (String) response.get("id");}
    @Override
    public String getProvider(){return ProviderType.NAVER.getValue();}
}
