package com.practice_back.entity.Oauth2;

import com.practice_back.response.ProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class GoogleUserInfo implements Oauth2UserInfo{
    private final Map<String, Object> attributes;
    public GoogleUserInfo(OAuth2User oAuth2User){
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
        return (String) attributes.get("picture");
    }
    @Override
    public String getProviderId(){return (String) attributes.get("sub");}
    @Override
    public String getProvider(){return ProviderType.GOOGLE.getValue();}
}
