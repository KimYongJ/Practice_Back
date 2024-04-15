package com.practice_back.entity.Oauth2;

import com.practice_back.response.ProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class GithubUserInfo implements Oauth2UserInfo{

    private final Map<String, Object> attributes;
    public GithubUserInfo(OAuth2User oAuth2User){
        this.attributes = oAuth2User.getAttributes();
    }
    @Override
    public Map<String, Object> getAttributes(){
        return attributes;
    }

    @Override
    public String getName(){
        return (String) attributes.get("name");
    }
    @Override
    public String getEmail(){
        return (String) attributes.get("email");
    }
    @Override
    public String getPicture(){
        return (String) attributes.get("avatar_url");
    }
    @Override
    public String getProviderId(){ return String.valueOf(attributes.get("id"));}
    @Override
    public String getProvider(){return ProviderType.GITHUB.getValue();}
}
