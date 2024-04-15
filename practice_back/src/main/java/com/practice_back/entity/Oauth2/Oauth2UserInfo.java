package com.practice_back.entity.Oauth2;


import java.util.Map;

/**
* 팩토리 메서드 패턴을 위한 상위타입 인터페이스
* */
public interface Oauth2UserInfo {
     Map<String, Object> getAttributes();
     String getName();
     String getEmail();
     String getPicture();
     String getProviderId();
     String getProvider();
}
