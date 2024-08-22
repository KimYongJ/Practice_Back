package com.practice_back.mockClass;

import com.practice_back.entity.Oauth2.Oauth2UserInfo;
import com.practice_back.response.ProviderType;

import java.util.Map;

public class MockOauth2UserInfo implements Oauth2UserInfo {
    private Map<String, Object> attributes;
    public MockOauth2UserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
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
